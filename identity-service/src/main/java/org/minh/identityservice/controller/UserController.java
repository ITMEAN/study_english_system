package org.minh.identityservice.controller;

import lombok.RequiredArgsConstructor;
import org.minh.identityservice.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private final UserService userService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllUser(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

}
