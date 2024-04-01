package com.ashish.Authservice.otp.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class OtpService {
    @Autowired
    JavaMailSender sender;

    public void send(String toEmailAdd,String otp){

        SimpleMailMessage smm=new SimpleMailMessage();
//        smm.setFrom("ashishs24199@gmail.com");
        smm.setTo(toEmailAdd);
        smm.setSubject("This is for verification");
        smm.setText("Your OTP is "+otp);
        sender.send(smm);

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
