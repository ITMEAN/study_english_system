package org.minh.flashcardservice.controller;

import org.minh.flashcardservice.dto.request.AddFlashCardUser;
import org.minh.flashcardservice.exception.BadCredentialsException;
import org.minh.flashcardservice.exception.S3UploadException;
import org.minh.flashcardservice.dto.request.AddVocabularyRequestDTO;
import org.minh.flashcardservice.dto.request.CreateFlashCardRequestDTO;
import org.minh.flashcardservice.dto.request.RemoveVocabularyRequestDTO;
import org.minh.flashcardservice.service.FlashCardUser.FlashCardUserService;
import org.minh.flashcardservice.service.flashcard.FlashCardService;
import org.minh.flashcardservice.service.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/flashcard")
public class FlashCardController {
    @Autowired
    private FlashCardService flashCardService;
    @Autowired
    private FlashCardUserService flashCardUserService;
    @Autowired
    private  JwtUtil jwtUtil;

    @PostMapping("/create")
    public ResponseEntity<?> createFlashCard(@RequestBody CreateFlashCardRequestDTO request, @RequestHeader("Authorization") String token) throws BadCredentialsException {
        return ResponseEntity.ok(flashCardService.addFlashCard(request, token));
    }

    @PostMapping("/add-vocabulary")
    public ResponseEntity<?> addVocabulary(@ModelAttribute AddVocabularyRequestDTO request,@RequestHeader("Authorization") String authorization) throws BadCredentialsException, S3UploadException {
        return ResponseEntity.ok(flashCardService.AddVocabulary(request,authorization));
    }

    @PostMapping("/remove-vocabulary")
    public ResponseEntity<?> removeVocabulary(@RequestBody RemoveVocabularyRequestDTO request , @RequestHeader("Authorization") String authorization) throws BadCredentialsException {
        return ResponseEntity.ok(flashCardService.removeVocabulary(request,authorization));
    }

    @GetMapping("/get-flashcards/{email}")
    public ResponseEntity<?> getFlashCardsRoleAdmin(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "5") int size, @PathVariable String email, @RequestHeader("Authorization") String authorization) throws BadCredentialsException {
        return ResponseEntity.ok(flashCardService.getFlashCardsByEmail(page, size, email, authorization));
    }
    @DeleteMapping("/remove-flashcard/{idFlashCard}")
    public ResponseEntity<?> removeFlashCard(@PathVariable String idFlashCard) {
        flashCardService.removeFlashCard(idFlashCard);
        return ResponseEntity.ok("Remove flashcard successfully");
    }

    @GetMapping("/user/get-flashcards-public")
    public ResponseEntity<?> getFlashCardsPublic(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "5") int size) throws BadCredentialsException {
        return ResponseEntity.ok(flashCardService.getFlashCardsPublic(page, size));
    }

    @GetMapping("/user/find-flashcard/{id}")
    public ResponseEntity<?> findFlashCardById(@PathVariable String id) throws BadCredentialsException {
        return ResponseEntity.ok(flashCardService.findFlashCardById(id));
    }

    @GetMapping("/user/find-flashcard-learned/{email}")
    public ResponseEntity<?> findFlashCardLearned(@PathVariable String email) {
        return ResponseEntity.ok(flashCardService.findFlashCardLearned(email));
    }

    @GetMapping("/user/find-flashCardUser/{email}/{idFlashCardSet}")
    public ResponseEntity<?> findFlashCardUser(@PathVariable String email, @PathVariable String idFlashCardSet) {
        return ResponseEntity.ok(flashCardUserService.findByEmailAndIdFlashCardSet(email, idFlashCardSet));
    }

    @PostMapping("/user/add-flashCardUser")
    public ResponseEntity<?> addFlashCardUser(@RequestBody AddFlashCardUser request, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(flashCardUserService.addFlashCardUser(request, token));
    }


}
