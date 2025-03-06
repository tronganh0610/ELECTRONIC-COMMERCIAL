package com.shop.sport.auth;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class AuthenicationResponse {
    private static AuthenicationResponse  instance;

    private AuthenicationResponse() {
        // private constructor to prevent instantiation
    }

    public static synchronized AuthenicationResponse getInstance() {
        if (instance == null) {
            instance = new AuthenicationResponse();
        }
        return instance;
    }

    public ResponseEntity<Object> generateAuthenicationResponse(String message, HttpStatus status, Object responseObj, String token) {
        Map<String, Object> map = new HashMap<>();
        map.put("message", message);
        map.put("status", status.value());
        map.put("data", responseObj);
        map.put("token", token);

        return new ResponseEntity<>(map, status);
    }
}