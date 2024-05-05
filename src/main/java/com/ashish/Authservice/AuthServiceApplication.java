package com.ashish.Authservice;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;

import static org.springframework.boot.SpringApplication.*;

@SpringBootApplication
@EnableFeignClients
public class AuthServiceApplication {

	public static void main(String[] args) {
		run(AuthServiceApplication.class, args);
	}

}
