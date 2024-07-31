package org.minh.flashcardservice.repositories;

import org.minh.flashcardservice.entity.FlashCardUser;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface FlashCardUserRepository extends MongoRepository<FlashCardUser, String> {
    List<FlashCardUser> findAllByEmail(String email);
    Optional<FlashCardUser> findByIdFlashCardSet (String idFlashCardSet);
    Optional<FlashCardUser> findByEmailAndIdFlashCardSet(String email, String idFlashCardSet);
}
