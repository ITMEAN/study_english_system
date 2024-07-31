package org.minh.userservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.minh.userservice.model.request.CreateUserRequest;
import org.minh.userservice.model.request.UpdateUserRequest;
import org.minh.userservice.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PutMapping("/update-user")
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserRequest user) {
           return new ResponseEntity<>(userService.updateUser(user), HttpStatus.OK);
    }

    @GetMapping("/{email}")
    public ResponseEntity<?> getUserById(@PathVariable("email") String email) {
            return new ResponseEntity<>(userService.getUserById(email), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers() {
            return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }



}
