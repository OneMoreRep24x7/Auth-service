package com.ashish.Authservice.configuration;

import com.ashish.Authservice.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "authority-service")
public interface AuthorityProxy {

    @PostMapping("/api/v1/user/register")
    public void userRegistration(@RequestBody UserDto userDto);

}
