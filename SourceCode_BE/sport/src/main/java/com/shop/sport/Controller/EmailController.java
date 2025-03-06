package com.shop.sport.Controller;


import com.shop.sport.MailService.EmailDetails;
import com.shop.sport.MailService.EmailService;
import com.shop.sport.Response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Annotation
@RestController
@RequestMapping("/api/v1/sentMail")
// Class
public class EmailController {

    Response response = Response.getInstance();

    @Autowired private EmailService emailService;

    // Sending a simple Email
    @PostMapping("/sendMail-simple")
    public ResponseEntity<Object>
    sendMail(@RequestBody EmailDetails details)
    {
        try {
            Boolean status
                    = emailService.sendSimpleMail(details);
            if (status)
            return response.generateResponse("sent mail successfully", HttpStatus.OK, status);
            else return response.generateResponse("sent mail fail", HttpStatus.OK, status);

        }catch (Exception e) {
            return response.generateResponse("sent mail fail"+e.getMessage(), HttpStatus.BAD_REQUEST, true);
        }

    }

    // Sending email with attachment
    @PostMapping("/sendMailWithAttachment")
    public ResponseEntity<Object> sendMailWithAttachment(
            @RequestBody EmailDetails details)
    {

        try {
            Boolean status
                    = emailService.sendMailWithAttachment(details);
            if (status)
                return response.generateResponse("sent mail successfully", HttpStatus.OK, status);
            else return response.generateResponse("sent mail fail", HttpStatus.OK, status);

        }catch (Exception e) {
            return response.generateResponse("sent mail fail"+e.getMessage(), HttpStatus.BAD_REQUEST, true);
        }
    }
}
