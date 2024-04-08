package com.ashish.Authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TrainerDto {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String role;

}
