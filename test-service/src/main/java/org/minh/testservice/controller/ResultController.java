package org.minh.testservice.controller;

import org.minh.testservice.exception.BadCredentialsException;
import org.minh.testservice.service.result.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/results")
public class ResultController {
    @Autowired
    private ResultService resultService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getResultById(@PathVariable("id") String id, @RequestHeader("Authorization") String token) throws BadCredentialsException {

        return new ResponseEntity<>(resultService.findResultTestById(id,token), HttpStatus.OK);
    }




}
