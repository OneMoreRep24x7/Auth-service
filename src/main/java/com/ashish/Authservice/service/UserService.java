package com.ashish.Authservice.service;

import com.ashish.Authservice.dto.AuthRequest;
import com.ashish.Authservice.dto.AuthResponse;
import com.ashish.Authservice.dto.RegisterRequest;
import com.ashish.Authservice.model.User;

import java.util.Optional;

public interface UserService {

    public Optional<User> findUserByEmail(String email);


    AuthResponse register(RegisterRequest registerRequest);

    AuthResponse authenticate(AuthRequest authRequest);
}
