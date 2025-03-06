package com.shop.sport.Response;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class Response {
    private static Response instance;

    private Response() {
        // private constructor to prevent instantiation
    }

    public static synchronized Response getInstance() {
        if (instance == null) {
            instance = new Response();
        }
        return instance;
    }

    public ResponseEntity<Object> generateResponse(String message, HttpStatus status, Object responseObj) {
        Map<String, Object> map = new HashMap<>();
        map.put("message", message);
        map.put("status", status.value());
        map.put("data", responseObj);

        return new ResponseEntity<>(map, status);
    }
}