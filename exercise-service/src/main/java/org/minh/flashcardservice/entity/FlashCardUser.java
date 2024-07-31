package org.minh.flashcardservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
@Document(collection = "flash_card_user")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FlashCardUser {
    private String id;
    private String email;
    private String idFlashCardSet;
    private List<String> idVocabulariesLearned = new ArrayList<>();
    private List<TestFlashCard> testFlashCards = new ArrayList<>();
}

