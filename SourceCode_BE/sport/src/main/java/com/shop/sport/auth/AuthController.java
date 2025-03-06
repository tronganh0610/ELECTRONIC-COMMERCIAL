package com.shop.sport.auth;

import com.shop.sport.Entity.Role;
import com.shop.sport.Entity.User;
import com.shop.sport.Response.Response;
import com.shop.sport.Service.FileUpload;
import com.shop.sport.Service.JwtService;
import com.shop.sport.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final JwtService jwtService;

    Response responseHandler = Response.getInstance();
    AuthenicationResponse authenicationResponse = AuthenicationResponse.getInstance();

    private final AuthenticationService authenticationService;

    @PostMapping(value = "/register")
    public ResponseEntity<Object> register(@RequestBody RegisterRequest request){


        if (authenticationService.checkUserExist(request.getUsername()))
            return responseHandler.generateResponse("User exist!", HttpStatus.UNAUTHORIZED, null);


        request.setRole(Role.CUSTOMER);
        User users = authenticationService.register(request);
        return responseHandler.generateResponse("Register Successfully !", HttpStatus.OK, users);

    }


    @PostMapping(value = "/getUser-byUserName")
    public ResponseEntity<Object> byUserName(@RequestBody Map<String,String> request){


        String username = request.get("username");
        List<String> idUser = authenticationService.getIdByUserName(username);
        if (idUser == null)
            return responseHandler.generateResponse("User not exist!", HttpStatus.BAD_REQUEST, null);
        else
        return responseHandler.generateResponse(" Successfully !", HttpStatus.OK, idUser);

    }

    @PostMapping(value = "/login")
    public ResponseEntity<Object> authenticate(@RequestBody AuthenticationRequest request){
        try {

            if(!authenticationService.checkUserExist(request.getUsername())){
                return authenicationResponse.generateAuthenicationResponse("username or password invalid", HttpStatus.BAD_REQUEST, null, null);

            }

            authenticationService.authenticate(request.getUsername(), request.getPassword());
            final Optional<User> user = authenticationService.loadUserByUserName(request.getUsername());
            UserDetails userDetails = user.get();



//            final String token = authenticationService.generateToken(request);
            final String jwtToken = jwtService.generateToken(userDetails);
            return authenicationResponse.generateAuthenicationResponse("login successfully !", HttpStatus.OK, user, jwtToken);
        } catch (Exception e) {
            return authenicationResponse.generateAuthenicationResponse("username or password invalid", HttpStatus.BAD_REQUEST, null, null);


        }

    }



    @Autowired
    private FileUpload fileUpload;

    @RequestMapping("/")
    public String home(){
        return "home";
    }

    @PostMapping("/upload")
    public Map<String, String> uploadFile(@RequestParam("image") MultipartFile multipartFile,
                             Model model) throws IOException {
        Map<String, String> a = fileUpload.uploadFile(multipartFile);
        return a;
    }

    @PostMapping("/delete_image")
    public Boolean deleteFile(
            @RequestBody Map<String, String> body ) throws IOException {

        return fileUpload.deleteFile(body.get("public_id"));
    }
}
