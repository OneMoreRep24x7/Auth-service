package com.ashish.Authservice.controller;

import com.ashish.Authservice.configuration.Oauth.dto.MessageDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class privateController {

    public ResponseEntity<MessageDto> privateMessages(
            @AuthenticationPrincipal(expression = "name")
            String name){

        return ResponseEntity.ok(new MessageDto("This is the message from form api protect "+name));

    }
}
