package com.ashish.Authservice.service;

import com.ashish.Authservice.configuration.feignClient.AuthorityProxy;
import com.ashish.Authservice.configuration.JwtService;
import com.ashish.Authservice.dto.*;
import com.ashish.Authservice.model.Role;
import com.ashish.Authservice.model.User;
import com.ashish.Authservice.otp.model.Otp;
import com.ashish.Authservice.otp.repository.OtpRepository;
import com.ashish.Authservice.otp.utils.OtpService;
import com.ashish.Authservice.repository.UserRepository;
import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
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


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


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
    private OtpRepository otpRepository;
    @Autowired
    private OtpService otpService;

    @Autowired
    private AuthorityProxy authorityProxy;



    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public RegisterResponse register(RegisterRequest registerRequest) throws MessagingException {

        Optional<User> optionalUser = userRepository.findByEmail(registerRequest.getEmail());
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            if(user.getIsVerified()){
                return RegisterResponse.builder()
                        .message("User already exist")
                        .statusCode(HttpStatus.CONFLICT.value())
                        .build();
            }else {
                Optional<Otp> optionalOtp = otpRepository.findByUser(user);
                if(optionalOtp.isPresent()){
                    Otp otp = optionalOtp.get();
                    otpRepository.delete(otp);
                }
                userRepository.delete(user);
            }

        }
            var user = User.builder()
                    .firstName(registerRequest.getFirstName())
                    .lastName(registerRequest.getLastName())
                    .email(registerRequest.getEmail())
                    .password(passwordEncoder.encode(registerRequest.getPassword()))
                    .role(Role.USER)
                    .isPremium(false)
                    .trialValid(LocalDateTime.now().plusDays(6))
                    .build();

        var savedUser = userRepository.save(user);
        otpManagement(user);
        return RegisterResponse.builder()
                .message("Otp send successfully")
                .statusCode(HttpStatus.OK.value())
                .user(user)
                .role(String.valueOf(user.getRole()))
                .build();

    }

    @Override
    public RegisterResponse registerTrainer(RegisterRequest registerRequest) throws MessagingException {
        Optional<User> optionalUser = userRepository.findByEmail(registerRequest.getEmail());
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            if(user.getIsVerified()){
                return RegisterResponse.builder()
                        .message("User already exist")
                        .statusCode(HttpStatus.CONFLICT.value())
                        .build();
            }else {
                Optional<Otp> optionalOtp = otpRepository.findByUser(user);
                if(optionalOtp.isPresent()){
                    Otp otp = optionalOtp.get();
                    otpRepository.delete(otp);
                }
                userRepository.delete(user);
            }

        }
        var user = User.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.TRAINER)
                .isPremium(false)
                .trialValid(LocalDateTime.now().plusDays(6))
                .build();

        var savedUser = userRepository.save(user);
        otpManagement(user);
        return RegisterResponse.builder()
                .message("Otp send successfully")
                .statusCode(HttpStatus.OK.value())
                .user(user)
                .role(String.valueOf(user.getRole()))
                .build();
    }

    @Override
    public void updatePayment(PaymentData paymentData) {
        UUID userId = paymentData.getUserId();
        User user = userRepository.findById(userId).get();
        LocalDateTime now = LocalDateTime.now();
        if(paymentData.getAmount() == 800){
            user.setTrialValid(now.plusMonths(6));
            user.setPremium(true);
        }else{
            user.setTrialValid(now.plusMonths(1));
            user.setPremium(true);
        }
        userRepository.save(user);
    }

    private void otpManagement(User user) throws MessagingException {
        String otp = otpService.generateOtp();
        otpService.send(user.getEmail(), otp);
        var generatedOtp = Otp.builder()
                .otp(otp)
                .time(LocalDateTime.now())
                .user(user)
                .build();
        otpRepository.save(generatedOtp);
    }

    @Override
    public RegisterResponse verifyOtp(OtpRequest otpRequest) {
        String email = otpRequest.getEmail();
        User user = userRepository.findByEmail(email).get();
        Otp otp = otpRepository.findByUser(user).get();
        if(otp.getOtp().equals(otpRequest.getOtp())){
            //valid  for one day
            if( Duration.between(otp.getTime(),LocalDateTime.now()).getSeconds()<=86400){
                user.setIsVerified(true);
                User savedUser = userRepository.save(user);
                otpRepository.delete(otp);
                if(String.valueOf(savedUser.getRole())== "USER"){
                    UserDto userDto = UserDto.builder()
                            .id(savedUser.getId())
                            .firstName(savedUser.getFirstName())
                            .lastName(savedUser.getLastName())
                            .email(savedUser.getEmail())
                            .role(String.valueOf(savedUser.getRole()))
                            .isPremium(false)
                            .trialValid(savedUser.getTrialValid())
                            .build();

                    authorityProxy.userRegistration(userDto);
                }else if (String.valueOf(savedUser.getRole())== "TRAINER"){
                    TrainerDto trainerDto = TrainerDto.builder()
                            .id(savedUser.getId())
                            .firstName(savedUser.getFirstName())
                            .lastName(savedUser.getLastName())
                            .email(savedUser.getEmail())
                            .role(String.valueOf(savedUser.getRole()))
                            .build();
                    authorityProxy.registerTrainer(trainerDto);
                }


                return RegisterResponse.builder()
                        .message("Otp Verified successfully..")
                        .statusCode(HttpStatus.OK.value())
                        .user(savedUser)
                        .isVerified(true)
                        .role(String.valueOf(savedUser.getRole()))
                        .build();
            }else {
                return RegisterResponse.builder()
                        .message("Otp Expired..")
                        .statusCode(HttpStatus.FORBIDDEN.value())
                        .user(user)
                        .isVerified(false)
                        .role(String.valueOf(user.getRole()))
                        .build();
            }
        }

        return RegisterResponse.builder()
                .message("Invalid otp")
                .statusCode(HttpStatus.FORBIDDEN.value())
                .user(user)
                .isVerified(false)
                .build();
    }



    @Override
    public LoginResponse authenticate(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword())
            );

            var user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow();
            var jwtToken = jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken(user);
            return LoginResponse.builder()
                    .user(user)
                    .accessToken(jwtToken)
                    .refreshToken(refreshToken)
                    .message("loginRequest successful..")
                    .statusCode(HttpStatus.OK.value())
                    .role(String.valueOf(user.getRole()))
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
            var authResponse = RegisterResponse.builder()
                    .message("")
                    .statusCode(1)
                    .build();
            new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
        }
    }
}

    @Override
    public void validateToken(String token) {
        jwtService.validateToken(token);
    }










}

