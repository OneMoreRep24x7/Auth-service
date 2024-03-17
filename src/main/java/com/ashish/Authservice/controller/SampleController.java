package com.ashish.Authservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/check")
public class SampleController {
    @GetMapping("/hello")
    public String hello(){
        return "Hello from auth service secured";
    }
}
