package com.shop.sport.Controller;

import com.shop.sport.Entity.Role;
import com.shop.sport.Entity.User;
import com.shop.sport.MailService.EmailDetails;
import com.shop.sport.MailService.EmailService;
import com.shop.sport.Response.Response;
import com.shop.sport.Service.FileUpload;
import com.shop.sport.Service.UserService;
import com.shop.sport.Utils.Utils;
import com.shop.sport.auth.AuthenticationService;
import com.shop.sport.auth.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    Response response = Response.getInstance();
    @Autowired
    private FileUpload fileUpload;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserService userService;
    @Autowired
    private  AuthenticationService authenticationService;
    @Autowired
    private  PasswordEncoder passwordEncoder;


    @GetMapping("/getAllEmployee")
    public ResponseEntity<Object> getAllEmployeeByAdmin() {

        try {
            List<User> users = userService.getAllUserByRole(String.valueOf(Role.EMPLOYEE));

            return response.generateResponse("Get All employee Successfully", HttpStatus.OK, users);
        } catch (Exception e) {
            return response.generateResponse("Get All employee fail"+e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }


    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<Object> findUserbyId(
            @PathVariable long id
    ) {

        try {
            User users = userService.getUserById(id);

            return response.generateResponse("Get All employee Successfully", HttpStatus.OK, users);
        } catch (Exception e) {
            return response.generateResponse("Get All employee fail"+e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }


    }


    @PostMapping("/search/employee")
    public ResponseEntity<Object> searchEmployee(   @RequestBody Map<String, String> body) {

        try {
            List<User> users = userService.searchEmployee(body.get("query"));

            return response.generateResponse("searchEmployee Successfully", HttpStatus.OK, users);
        } catch (Exception e) {
            return response.generateResponse("searchEmployee fail"+e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }


    }

    @PostMapping("/search/customer")
    public ResponseEntity<Object> searchCustomer(   @RequestBody Map<String, String> body) {

        try {
            List<User> users = userService.searchCustomer(body.get("query"));

            return response.generateResponse("searchCustomer Successfully", HttpStatus.OK, users);
        } catch (Exception e) {
            return response.generateResponse("searchCustomer fail"+e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }


    }

    @GetMapping("/getAllCustomer")
    public ResponseEntity<Object> getAllCustomerByAdmin() {

        try {
//            List<User> users = userService.getAllUsers();
            List<User> users = userService.getAllUserByRole(String.valueOf(Role.CUSTOMER));

            return response.generateResponse("Get All CUSTOMER Successfully", HttpStatus.OK, users);
        } catch (Exception e) {
            return response.generateResponse("Get All CUSTOMER fail in UserController.java" + e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<Object> deleteAcount(
            @PathVariable long id
    ) {
        try {
            if(userService.checkDeleteUser(id) == 0) {
                return response.generateResponse("Tài khoản đã không thể xóa !", HttpStatus.OK, null);

            }
            User users = userService.updateStatusUser(id);
            return response.generateResponse("remove user Successfully", HttpStatus.OK, users);
        } catch (Exception e) {
            return response.generateResponse("remove user fail in UserController.java" + e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<Object> updateUserByAdmin(
            @RequestBody Map<String, String> body,
           @PathVariable long id
    ) {
        try {
            User updateUser = userService.getUserById(id);
            String fullname = body.get("fullname");
            String email = body.get("email");
            String sdt = body.get("sdt");

            System.out.println(email);
            if ( fullname != null) {
                updateUser.setFullname(fullname);
            }
            if (email != null) {
                updateUser.setEmail(email);
            }
            if (sdt != null) {
                updateUser.setPhone(sdt);
            }
            userService.updateUser(updateUser);


            return response.generateResponse("Cập nhật tài khoản thành công", HttpStatus.OK, updateUser);
        } catch (Exception e) {
            return response.generateResponse("Cập nhật tài khoản fail in UserController.java" + e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }


    @PostMapping("/change-password/{id}")
    public ResponseEntity<Object> changePassword(
            @RequestBody Map<String, String> body,
            @PathVariable long id
    ) {
        try {

            User updateUser = userService.getUserById(id);
            String password = body.get("newpassword");
            updateUser.setPassword(passwordEncoder.encode(password));
            userService.updateUser(updateUser);


            return response.generateResponse("Cập nhật tài khoản thành công", HttpStatus.OK, updateUser);
        } catch (Exception e) {
            return response.generateResponse("Cập nhật tài khoản fail in UserController.java" + e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }




    @PatchMapping("/update/{id}")
    public ResponseEntity<Object> updateProfile(@PathVariable long id,
                                                @RequestParam(value = "email", required = false) String email,
                                                @RequestParam(value = "fullname", required = false) String fullname,
                                                @RequestParam(value = "phone", required = false) String phone,
                                                @RequestParam(value = "adress", required = false) String adress,
                                                @RequestParam(value = "birthday", required = false) String birthday,
                                                @RequestParam(value = "image" , required = false) MultipartFile multipartFile

    ) {

        try {
//            System.out.println(multipartFile+"------------");
            User updateUser = userService.getUserById(id);
            if(updateUser==null) {
                return response.generateResponse("user not found", HttpStatus.BAD_REQUEST, null);
            }else {
                if (fullname != null) {
                    updateUser.setFullname(fullname);
                }
                if (phone != null) {
                    updateUser.setPhone(phone);
                }

                if (adress != null) {
                    updateUser.setAdress(adress);
                }
                if (email != null) {
                    updateUser.setEmail(email);
                }

                if (birthday != null) {

                    updateUser.setBirthday(Utils.convertStringToDate(birthday));
                }
                if (multipartFile!=null) {

                    if (updateUser.getPublicId() != null) {
                        fileUpload.deleteFile(updateUser.getPublicId());
                    }
                    Map<String, String> upload = fileUpload.uploadFile(multipartFile);

                    updateUser.setAvatarUrl(upload.get("url"));
                    updateUser.setPublicId(upload.get("public_id"));

                }

                updateUser.setStatus(true);
                userService.updateUser(updateUser);
                return response.generateResponse("update profile successfully", HttpStatus.OK, updateUser);

            }
        } catch (Exception e) {
            // TODO: handle exception
            return response.generateResponse("update profile failed"+e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }

    }


    @PostMapping("/createAccEmployee")
    public ResponseEntity<Object> createAdmin(
            @RequestBody Map<String, String> body
    ) {

        try {

            if(userService.checkUserExist(body.get("email")) ){
                return response.generateResponse("tài khoản đã tồn tại" , HttpStatus.BAD_REQUEST, true);

            }

            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.setEmail(body.get("email"));
            registerRequest.setRole(Role.EMPLOYEE);
            registerRequest.setUsername(body.get("email"));
            String randomPassWord = Utils.generateRandomString(6);
            registerRequest.setPassword(randomPassWord);


            if (authenticationService.checkUserExist(registerRequest.getUsername()))
                return response.generateResponse("User exist!", HttpStatus.OK, null);


            //sentmail
            try {
                EmailDetails details = new EmailDetails();
                details.setSubject("THÔNG TIN TÀI KHOẢN NHÂN VIÊN");
                details.setMsgBody("Chúc Mừng Bạn Đã Trở Thành Nhân Viên Của Cửa Hàng Thể Thao" +
                        "\n\nTài Khoản Được Cấp Của Bạn Là :"+"\n\nUserName:"+registerRequest.getUsername()+"\nMật Khẩu:"+ randomPassWord + "\n\nVui lòng đăng nhập và đổi mật khẩu");
                details.setRecipient(registerRequest.getEmail());
                Boolean status
                        = emailService.sendSimpleMail(details);;
                User users = authenticationService.register(registerRequest);
                return response.generateResponse("Provide accout Successfully !", HttpStatus.OK, users);
            } catch (Exception e) {
                return response.generateResponse("sent mail failed Provide accout failed" + e.getMessage(), HttpStatus.OK, true);
            }


        } catch (Exception e) {
            return response.generateResponse("Provide accout failed" + e.getMessage(), HttpStatus.OK, null);
        }
    }

    @PostMapping("/forget-password/{id}")
    public ResponseEntity<Object> forgetPass(
            @RequestBody Map<String, String> body,
            @PathVariable long id
    ) {

        try {

          String email = body.get("email");

            User updateUser = userService.getUserById(id);
            String password = Utils.generateRandomString(6);
            updateUser.setPassword(passwordEncoder.encode(password));
            userService.updateUser(updateUser);
            //sentmail
            try {
                EmailDetails details = new EmailDetails();
                details.setSubject("Cấp Lại Mật Khẩu !!!");
                details.setMsgBody("Bạn đã yêu cầu cấp lại mật khẩu mới" +
                        "\n\nĐây là mật khẩu mới của bạn :"+ password +"\n\nVui lòng đăng nhập và đổi mật khẩu");
                details.setRecipient(email);
                Boolean status
                        = emailService.sendSimpleMail(details);;

                return response.generateResponse("Provide accout Successfully !", HttpStatus.OK, status);
            } catch (Exception e) {
                return response.generateResponse("sent mail failed Provide accout failed" + e.getMessage(), HttpStatus.BAD_REQUEST, true);
            }


        } catch (Exception e) {
            return response.generateResponse("Provide accout failed" + e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

}
