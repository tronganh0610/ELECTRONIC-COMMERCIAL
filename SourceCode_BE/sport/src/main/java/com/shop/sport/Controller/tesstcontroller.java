package com.shop.sport.Controller;

import com.shop.sport.Entity.User;
import com.shop.sport.Service.FileUpload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/test")
public class tesstcontroller {

    @GetMapping
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("helo");
    }


    @GetMapping("/tutorials/{id}")
    public ResponseEntity<String> getTutorialById(@PathVariable("id") long id) {
        return ResponseEntity.ok("helo");

    }


}
