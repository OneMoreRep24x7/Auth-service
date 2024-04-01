package com.ashish.Authservice.controller;

import com.ashish.Authservice.configuration.Oauth.dto.TokenDto;
import com.ashish.Authservice.configuration.Oauth.dto.UrlDto;
import com.ashish.Authservice.dto.*;
import com.ashish.Authservice.service.AuthService;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Value("${spring.security.oauth2.resourceserver.opaque-token.clientId}")
    private String clientId;

    @Value("${spring.security.oauth2.resourceserver.opaque-token.clientSecret}")
    private String clientSecret;

    @GetMapping("/url")
    public ResponseEntity<UrlDto> googleAuth(){
        //below url will generate the login form of the google
        String url = new GoogleAuthorizationCodeRequestUrl(clientId,
                "http://localhost:4200",
                Arrays.asList(
                        "email",
                        "profile",
                        "openid")).build();

        return ResponseEntity.ok(new UrlDto(url));
    }

    @GetMapping("/auth/callback")
    public ResponseEntity<TokenDto> callback(@RequestParam("code") String code) throws URISyntaxException {

        String token;
        try {
            token = new GoogleAuthorizationCodeTokenRequest(
                    new NetHttpTransport(), new GsonFactory(),
                    clientId,
                    clientSecret,
                    code,
                    "http://localhost:4200"
            ).execute().getAccessToken();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(new TokenDto(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register (
            @RequestBody RegisterRequest registerRequest
    ){
        var response = authService.register(registerRequest);

            return ResponseEntity.ok(response);


    }

    @PostMapping("/registerTrainer")
    public ResponseEntity<RegisterResponse> registerTrainer(
            @RequestBody RegisterRequest registerRequest
    ){
        return ResponseEntity.ok(authService.registerTrainer(registerRequest));
    }

       @GetMapping("/test")
       public ResponseEntity<TestResponse> test(){
        return ResponseEntity.ok(TestResponse.builder()
                        .message("Hello from test")
                        .build());
       }

    @PostMapping("/verifyOtp")
    public ResponseEntity<RegisterResponse> verifyOtp(@RequestBody OtpRequest otpRequest){
        return ResponseEntity.ok(authService.verifyOtp(otpRequest));
    }
    @PostMapping("/authenticate")
    public ResponseEntity<LoginResponse> authenticate (
            @RequestBody LoginRequest loginRequest
            ){
        return ResponseEntity.ok(authService.authenticate(loginRequest));
    }



    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        authService.refreshToken(request, response);
    }

    @GetMapping("/validate")
    public String validateToken(@RequestParam("token") String token) {
        authService.validateToken(token);
        return "Token is valid";
    }




}
