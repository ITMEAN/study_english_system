package org.minh.flashcardservice.service.flashcard;

import org.minh.flashcardservice.entity.FlashCard;
import org.minh.flashcardservice.entity.FlashCardUser;
import org.minh.flashcardservice.exception.BadCredentialsException;
import org.minh.flashcardservice.exception.S3UploadException;
import org.minh.flashcardservice.dto.request.AddVocabularyRequestDTO;
import org.minh.flashcardservice.dto.request.CreateFlashCardRequestDTO;
import org.minh.flashcardservice.dto.request.RemoveVocabularyRequestDTO;
import org.minh.flashcardservice.dto.response.ListFlashCardResponse;

import java.util.List;

public interface FlashCardService {
    FlashCard addFlashCard(CreateFlashCardRequestDTO request, String token) throws BadCredentialsException;

    FlashCard AddVocabulary(AddVocabularyRequestDTO request, String authorization) throws BadCredentialsException, S3UploadException;

    FlashCard removeVocabulary(RemoveVocabularyRequestDTO request, String authorization) throws BadCredentialsException;

    FlashCard findFlashCardById(String id) throws BadCredentialsException;

    ListFlashCardResponse getFlashCardsByEmail(int page, int size, String email, String authorization) throws BadCredentialsException;

    ListFlashCardResponse getFlashCardsPublic(int page, int size) throws BadCredentialsException;

    void removeFlashCard(String idFlashCard);

    List<FlashCard> findFlashCardByIds(List<String> ids);

    List<FlashCard> findFlashCardLearned(String email);
}

