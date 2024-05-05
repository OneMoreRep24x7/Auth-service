package com.ashish.Authservice.configuration.feignClient;

import com.ashish.Authservice.dto.TrainerDto;
import com.ashish.Authservice.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "authority-service",url ="http://15.206.80.123:8081")
public interface AuthorityProxy {

    @PostMapping("/api/v1/user/register")
    public void userRegistration(@RequestBody UserDto userDto);

    @PostMapping("/api/v1/trainer/register")
    public void registerTrainer(@RequestBody TrainerDto trainerDto);

}
