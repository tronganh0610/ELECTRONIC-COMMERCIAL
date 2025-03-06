package com.shop.sport.MailService;

public interface  EmailService {

    // Method
    // To send a simple email
    Boolean sendSimpleMail(EmailDetails details);

    // Method
    // To send an email with attachment
    Boolean sendMailWithAttachment(EmailDetails details);
}
