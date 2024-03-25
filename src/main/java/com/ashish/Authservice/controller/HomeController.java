package com.ashish.Authservice.controller;

import com.ashish.Authservice.dto.TestResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/check")
public class HomeController {


    @GetMapping("/hello")
    public ResponseEntity<TestResponse> hello(){
      var message =  TestResponse.builder()
                .message("Hello form the secured API")
                .build();
        return ResponseEntity.ok(message);
    }
}
