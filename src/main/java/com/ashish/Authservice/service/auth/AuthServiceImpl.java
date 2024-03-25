package com.ashish.Authservice.service.auth;

import com.ashish.Authservice.configuration.JwtService;
import com.ashish.Authservice.dto.AuthRequest;
import com.ashish.Authservice.dto.AuthResponse;
import com.ashish.Authservice.dto.LoginResponse;
import com.ashish.Authservice.dto.RegisterRequest;
import com.ashish.Authservice.model.Role;
import com.ashish.Authservice.model.User;
import com.ashish.Authservice.repository.UserRepository;
import com.ashish.Authservice.token.Token;
import com.ashish.Authservice.token.TokenRepository;
import com.ashish.Authservice.token.TokenType;
import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Optional;



@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenRepository tokenRepository;





    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public AuthResponse register(RegisterRequest registerRequest) {

        Optional<User> optionalUser = userRepository.findByEmail(registerRequest.getEmail());
        if(optionalUser.isPresent()){
            return AuthResponse.builder()
                    .message("User already exist")
                    .statusCode(HttpStatus.CONFLICT.value())
                    .build();
        }else{

            var user = User.builder()
                    .fistName(registerRequest.getFirstName())
                    .lastName(registerRequest.getLastName())
                    .email(registerRequest.getEmail())
                    .password(passwordEncoder.encode(registerRequest.getPassword()))
                    .role(Role.USER)
                    .phone(registerRequest.getPhone())
                    .build();

        var savedUser = userRepository.save(user);
        return AuthResponse.builder()
                .message("User registered successfully..")
                .statusCode(HttpStatus.OK.value())
                .build();
    }
}

    @Override
    public LoginResponse authenticate(AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getEmail(),
                            authRequest.getPassword())
            );

            var user = userRepository.findByEmail(authRequest.getEmail()).orElseThrow();
            var jwtToken = jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken(user);
            revokeAllUserTokens(user);
            saveUserToken(user, jwtToken);
            return LoginResponse.builder()
                    .user(user)
                    .accessToken(jwtToken)
                    .refreshToken(refreshToken)
                    .message("login successful..")
                    .statusCode(HttpStatus.OK.value())
                    .build();
        } catch (AuthenticationException e) {
            // Handle authentication failure
            return LoginResponse.builder()
                    .message("User not registered or incorrect password")
                    .statusCode(HttpStatus.FORBIDDEN.value())
                    .build();
        }
    }


@Override
public void refreshToken(
        HttpServletRequest request,
        HttpServletResponse response
) throws IOException {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String refreshToken;
    final String userEmail;
    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
        return;
    }
    refreshToken = authHeader.substring(7);
    userEmail = jwtService.extractUserName(refreshToken);
    if (userEmail != null) {
        var user = this.userRepository.findByEmail(userEmail)
                .orElseThrow();
        if (jwtService.isTokenValid(refreshToken, user)) {
            var accessToken = jwtService.generateToken(user);
            revokeAllUserTokens(user);
            saveUserToken(user, accessToken);
            var authResponse = AuthResponse.builder()
                    .message("")
                    .statusCode(1)
                    .build();
            new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
        }
    }
}



private void saveUserToken(User user, String jwtToken) {
    var token = Token.builder()
            .user(user)
            .token(jwtToken)
            .tokenType(TokenType.BEARER)
            .expired(false)
            .revoked(false)
            .build();
    tokenRepository.save(token);


}

private void revokeAllUserTokens(User user) {

    var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
    if (validUserTokens.isEmpty())
        return;
    validUserTokens.forEach(token -> {
        token.setExpired(true);
        token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
}



}

