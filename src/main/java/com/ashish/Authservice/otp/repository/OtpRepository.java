package com.ashish.Authservice.otp.repository;

import com.ashish.Authservice.model.User;
import com.ashish.Authservice.otp.model.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp,Long> {
    Optional<Otp> findByUser(User user);
}
