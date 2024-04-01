package com.ashish.Authservice.otp.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {

    @Value("${spring.mail.username}")
    private String username;
    @Value("${spring.mail.password}")
    private String password;
    @Value("${spring.mail.host}")
    private String host;
    @Value("${spring.mail.port}")
    private String port;


    @Bean
    public JavaMailSender javaMailSender(){
        JavaMailSenderImpl javaMailSender=new JavaMailSenderImpl();
        javaMailSender.setUsername(username);
        javaMailSender.setPassword(password);
        javaMailSender.setHost(host);
        javaMailSender.setPort(Integer.parseInt(port));

        Properties props = javaMailSender.getJavaMailProperties();
        props.put("mail.smtp.starttls.enable","true");
        return  javaMailSender;
    }
}
