package com.ashish.Authservice.otp.model;

import com.ashish.Authservice.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "Otp")
public class Otp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    String otp;

    LocalDateTime time;

    @OneToOne
    @JoinColumn(name = "USER_ID")
    User user;
}
