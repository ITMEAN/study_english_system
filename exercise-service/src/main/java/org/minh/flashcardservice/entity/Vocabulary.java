package org.minh.flashcardservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "vocabulary")
public class Vocabulary {
    @Id
    private String id;
    @Field
    private String word;
    @Field
    private String meaning;
    @Field
    private String example;
    @Field
    private String img;
    @Field
    private String audio;
    @Field
    private String spellings;
}
