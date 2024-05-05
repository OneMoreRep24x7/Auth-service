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
public class RegisterResponse {

    private String message;
    private int statusCode;
    private boolean isVerified;
    private User user;
    private String role;

}
