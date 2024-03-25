package com.ashish.Authservice.service.auth;

import com.ashish.Authservice.dto.AuthRequest;
import com.ashish.Authservice.dto.AuthResponse;
import com.ashish.Authservice.dto.LoginResponse;
import com.ashish.Authservice.dto.RegisterRequest;
import com.ashish.Authservice.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

public interface AuthService {

    public Optional<User> findUserByEmail(String email);


    AuthResponse register(RegisterRequest registerRequest);

    LoginResponse authenticate(AuthRequest authRequest);

    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;


}
