package org.minh.flashcardservice.dto.request;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RemoveVocabularyRequestDTO {
    private List<String> idVocabularies;
    private String idFlashCard;
}
