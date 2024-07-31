package org.minh.flashcardservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddFlashCardUser {
    private String email;
    private String idFlashCardSet;
}
