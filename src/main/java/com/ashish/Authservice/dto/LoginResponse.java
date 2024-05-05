package com.ashish.Authservice.dto;

import com.ashish.Authservice.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private User user;
    private String accessToken;
    private String refreshToken;
    private String message;
    private int statusCode;
    private String role;


}
