package org.minh.flashcardservice.repositories;

import org.minh.flashcardservice.entity.FlashCard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FlashCardRepository extends MongoRepository<FlashCard,String> {
    Page<FlashCard> findAllByCreatedBy(String createdBy, Pageable pageable);
}
