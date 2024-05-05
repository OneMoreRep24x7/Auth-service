package com.ashish.Authservice.otp.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class OtpService {
    @Autowired
    JavaMailSender sender;

    public void send(String toEmailAdd, String otp) throws MessagingException {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true); // true indicates multipart message

        helper.setTo(toEmailAdd);
        helper.setSubject("Verification Email from OneMoreRep");

        // Format email message with HTML
        String htmlContent = "<div style=\"padding: 20px; background-color: #f2f2f2; border-radius: 10px;\">"
                + "<h3 style=\"color: #333;\">Dear Fitness Enthusiast,</h3>"
                + "<p>Thank you for choosing OneMoreRep for your fitness journey.</p>"
                + "<p>Your <strong>OTP</strong> for verification is: <strong>" + otp + "</strong></p>"
                + "<p>Remember, success is not achieved overnight. Keep pushing your limits and strive for progress, not perfection.</p>"
                + "<p>Stay motivated, stay focused, and keep grinding!</p>"
                + "<p>Best regards,<br/>OneMoreRep Team</p>"
                + "</div>";

        // Set email message
        helper.setText(htmlContent, true); // true indicates HTML content

        // Send email
        sender.send(message);
    }

    public String generateOtp(){
        Random random =new Random();
        int randomNumber = random.nextInt(999999);
        String output =Integer.toString(randomNumber);
        while (output.length()<6){
            output="0"+ output;
        }
        return output;
    }
}
