package com.shop.sport.auth;


import com.shop.sport.Entity.User;
import com.shop.sport.Repositories.UserRepository;
import com.shop.sport.Service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    public Boolean checkUserExist(String userName){
        for (User user: getAllUsers()
        ) {
            if(userName.equals(user.getUsername()) && user.getStatus())
                return true;
        }
        return  false;

    }
    public List<String> getIdByUserName(String userName){
        List<String> result = new ArrayList<>();
        for (User user: getAllUsers()
        ) {
            if(userName.equals(user.getUsername()) && user.getStatus()) {
                result.add(String.valueOf(user.getId()));
                result.add(user.getEmail());
                return result;
            }

        }
        return  null;

    }

    public User register(RegisterRequest request) {
        User user = User.builder()
                .avatarUrl("https://res.cloudinary.com/dtjdyvzob/image/upload/v1717382945/tronganh/avatar/itf8iusk58nikqvmfryr.jpg")
                .status(true)
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();


        userRepository.save(user);
            return user;

//        var jwtToken = jwtService.generateToken(user);
//        return AuthenticationResponse.builder()
//                .token(jwtToken)
//                .build();

    }

    public Boolean authenticate(String username, String password) throws Exception {
        try {

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            return true;
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("tên tài khoản hoặc mật khẩu không chính xác", e);
        }
    }

    public Optional<User> loadUserByUserName(String username){
     return  userRepository.findByUsername(username);
    }

//    public String generateToken(AuthenticationRequest request) {
//
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        request.getUsername(),
//                        request.getPassword()
//                )
//        );
//        var user = userRepository.findByUsername(request.getUsername())
//                .orElseThrow();
//        if (user==null) return "0";
//
//        var jwtToken = jwtService.generateToken(user);
//        return jwtToken;
//    }
}
