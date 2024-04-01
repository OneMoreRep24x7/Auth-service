package com.ashish.Authservice.service;

import com.ashish.Authservice.dto.*;
import com.ashish.Authservice.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

public interface AuthService {

    public Optional<User> findUserByEmail(String email);


    RegisterResponse register(RegisterRequest registerRequest);

    LoginResponse authenticate(LoginRequest loginRequest);

    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;


    void validateToken(String token);

    RegisterResponse verifyOtp(OtpRequest otpRequest);

    RegisterResponse registerTrainer(RegisterRequest registerRequest);
}
