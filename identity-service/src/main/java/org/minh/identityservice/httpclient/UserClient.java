package org.minh.identityservice.httpclient;

import org.minh.identityservice.entity.Users;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
@Component
@FeignClient (name = "user-service", url = "http://localhost:8080")
public interface UserClient {
    @GetMapping(value = "/users/all",produces = MediaType.APPLICATION_JSON_VALUE)
    List<Users> getAllUsers();
}
