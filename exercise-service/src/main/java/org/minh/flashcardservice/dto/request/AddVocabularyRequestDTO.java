package org.minh.flashcardservice.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddVocabularyRequestDTO  implements Serializable {
    private String word;
    private String meaning;
    private String example;
    private MultipartFile img;
    private MultipartFile audio;
    private String spellings;
    private String idFlashCard;
}
