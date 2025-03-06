package com.shop.sport.Service;

import com.shop.sport.Entity.User;
import com.shop.sport.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    public List<User> getAllUserByRole(String role) {
        return  userRepository.getAllByRole(role);
    }

    public List<User> searchEmployee(String text) {
        return  userRepository.searchUser("EMPLOYEE", text);
    }

    public List<User> searchCustomer(String text) {
        return  userRepository.searchUser("CUSTOMER", text);
    }

    public Boolean checkUserExist(String userName){
        for (User user: getAllUsers()
        ) {
            if(userName.equals(user.getUsername())  && user.getStatus() )
                return true;
        }
        return  false;

    }

    public Long checkDeleteUser(long id) {
        Long result = userRepository.check_delete_user(id);
        return result;
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }


    public User updateStatusUser(Long id) {
        User user = getUserById(id);
        user.setStatus(false);
        userRepository.save(user);
        return user;
    }


    public User createUser(User user) {
//        if(user.getRole().equals("ADMIN"))
//            user.setRole(RoleEnum.ADMIN);
//        else user.setRole(RoleEnum.EMPLOYEE);
//        String passWord = passwordEncoder.encode(user.getPassword());
//        user.setPassword(passWord);
        return userRepository.save(user);
    }

    public User updateUser( User user) {
        return userRepository.save(user);
    }


}
