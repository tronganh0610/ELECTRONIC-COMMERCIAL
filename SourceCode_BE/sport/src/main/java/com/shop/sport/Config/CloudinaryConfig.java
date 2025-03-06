package com.shop.sport.Config;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;
@Configuration
public class CloudinaryConfig {

    @Value("${cloud_name}")
    private String CLOUD_NAME;
    @Value("${api_key}")
    private String API_KEY;
    @Value("${api_secret}")
    private String API_SECRET;
//    private final String CLOUD_NAME = "YOUR_CLOUD_NAME";
//    private final String API_KEY = "YOUR_API_KEY";
//    private final String API_SECRET = "YOUR_API_SECRET";
    @Bean
    public Cloudinary cloudinary(){
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name",CLOUD_NAME);
        config.put("api_key",API_KEY);
        config.put("api_secret",API_SECRET);
        return new Cloudinary(config);
    }
}