package com.shop.sport;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "API", version = "2.0", description = "Trying"))
@CrossOrigin(origins = "http://**")
public class SportApplication {

	public static void main(String[] args) {


		SpringApplication.run(SportApplication.class, args);

		System.err.println("/v3/api-docs");

	}

}
