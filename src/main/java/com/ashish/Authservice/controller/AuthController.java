package com.ashish.Authservice.controller;

import com.ashish.Authservice.dto.AuthRequest;
import com.ashish.Authservice.dto.AuthResponse;
import com.ashish.Authservice.dto.LoginResponse;
import com.ashish.Authservice.dto.RegisterRequest;
import com.ashish.Authservice.service.auth.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register (
            @RequestBody RegisterRequest registerRequest
    ){
        var response = authService.register(registerRequest);

            return ResponseEntity.ok(response);


    }
    @PostMapping("/authenticate")
    public ResponseEntity<LoginResponse> authenticate (
            @RequestBody AuthRequest authRequest
            ){
        return ResponseEntity.ok(authService.authenticate(authRequest));
    }



    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        authService.refreshToken(request, response);
    }



}
