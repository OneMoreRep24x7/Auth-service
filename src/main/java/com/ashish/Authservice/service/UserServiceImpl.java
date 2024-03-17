package com.ashish.Authservice.service;

import com.ashish.Authservice.configuration.JwtService;
import com.ashish.Authservice.dto.AuthRequest;
import com.ashish.Authservice.dto.AuthResponse;
import com.ashish.Authservice.dto.RegisterRequest;
import com.ashish.Authservice.model.Roles;
import com.ashish.Authservice.model.User;
import com.ashish.Authservice.repository.RoleRepository;
import com.ashish.Authservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserServiceImpl implements UserService{
 @Autowired
 private UserRepository userRepository;

 @Autowired
 private RoleRepository roleRepository;
 @Autowired
 private JwtService jwtService;
 @Autowired
 private AuthenticationManager authenticationManager;

 @Autowired
 private PasswordEncoder passwordEncoder;


    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public AuthResponse register(RegisterRequest registerRequest) {
        User user =  new User();
        user.setFistName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        Roles role = roleRepository.findById(1).orElseThrow();
        user.setRole(role);

        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public AuthResponse authenticate(AuthRequest authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmail(),
                        authRequest.getPassword())
        );
        var user = userRepository.findByEmail(authRequest.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }



}
