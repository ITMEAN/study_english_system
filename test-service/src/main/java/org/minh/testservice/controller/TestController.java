package org.minh.testservice.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.minh.testservice.exception.BadCredentialsException;
import org.minh.testservice.exception.S3UploadException;
import org.minh.testservice.model.request.test.*;
import org.minh.testservice.service.result.ResultService;
import org.minh.testservice.service.test.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tests")
public class TestController {
    @Autowired
    private final TestService testService;
    @Autowired
    private final ResultService resultService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@ModelAttribute AddTestRequest request) throws S3UploadException {
        return new ResponseEntity<>(testService.addTest(request), HttpStatus.CREATED);
    }
    @PostMapping("/add-part")
    public ResponseEntity<?> addPart(@RequestBody AddPartToTestRequest request) throws S3UploadException {
        return new ResponseEntity<>(testService.addPartToTest(request), HttpStatus.CREATED);
    }

    @PostMapping("/add-combo")
    public ResponseEntity<?> addComboQuestion(@ModelAttribute AddGroupQuestion request) throws S3UploadException {
        return new ResponseEntity<>(testService.addGroupQuestionToPart(request), HttpStatus.CREATED);
    }
    @PostMapping("/add-question")
    public  ResponseEntity<?> addQuestion(@ModelAttribute AddQuestionToPartRequest request) throws S3UploadException {
        return  new ResponseEntity<>(testService.addQuestionToPart(request), HttpStatus.CREATED);
    }
    @GetMapping("/all")
    public ResponseEntity<?> getAllTests(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size) throws S3UploadException {
        return new ResponseEntity<>(testService.getTests(page, size), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTestById(@PathVariable("id") String id) {
        return new ResponseEntity<>(testService.getTestById(id), HttpStatus.OK);
    }

    @PostMapping("/submit-test")
    public ResponseEntity<?> submitTest(@RequestBody SubmitTestRequest request) {
        return new ResponseEntity<>(resultService.submitTest(request), HttpStatus.OK);
    }
    @PostMapping("/result/{id}")
    public ResponseEntity<?> getResultById(@PathVariable("id") String id, @RequestHeader("Authorization") String token) throws BadCredentialsException {
        token = token.replace("Bearer ", "");
        return new ResponseEntity<>(resultService.findResultTestById(id,token), HttpStatus.OK);
    }

    @GetMapping("/history-result/{email}")
    private ResponseEntity<?> findHistoryResult (@PathVariable("email") String email, @RequestHeader("Authorization") String token) throws BadCredentialsException {
        return new ResponseEntity<>(resultService.getResultTestByEmail(email,token), HttpStatus.OK);
    }

    @GetMapping("/analytics/{email}")
    private ResponseEntity<?> getAnalytics (@PathVariable("email") String email, @RequestHeader("Authorization") String token) throws BadCredentialsException {
        return new ResponseEntity<>(resultService.getAnalytics(email,token), HttpStatus.OK);
    }







}
