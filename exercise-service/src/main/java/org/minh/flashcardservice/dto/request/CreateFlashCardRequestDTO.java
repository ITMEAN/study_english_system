package org.minh.flashcardservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class CreateFlashCardRequestDTO {
    private String name;
    private String description;
    private String createdBy;

}
