package org.minh.flashcardservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.minh.flashcardservice.entity.FlashCard;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListFlashCardResponse {
    private int total;
    private List<FlashCard> flashCards;
}
