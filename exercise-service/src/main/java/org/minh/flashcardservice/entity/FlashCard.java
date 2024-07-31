package org.minh.flashcardservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "flash-card")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlashCard {
    @Id
    private String id;
    @Field
    private String name;
    @Field
    private String description;
    @Field
    private String createdBy;
    @Field
    private boolean createByAdmin;
    @Field
    private String createName;
    @Field
    private List<Vocabulary> vocabularies = new ArrayList<>();
}
