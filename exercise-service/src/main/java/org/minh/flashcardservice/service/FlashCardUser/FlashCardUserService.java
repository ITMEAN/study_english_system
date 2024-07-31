package org.minh.flashcardservice.service.FlashCardUser;

import org.minh.flashcardservice.dto.request.AddFlashCardUser;
import org.minh.flashcardservice.entity.FlashCardUser;

import java.util.List;

public interface FlashCardUserService {
    FlashCardUser findFlashCardUserById(String id);
    FlashCardUser addFlashCardUser (AddFlashCardUser request,String token);
    List<FlashCardUser> findAllByEmail(String email);
    FlashCardUser findByEmailAndIdFlashCardSet(String email, String idFlashCardSet);

}
