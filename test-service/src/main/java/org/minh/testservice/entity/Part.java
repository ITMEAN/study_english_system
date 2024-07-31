package org.minh.testservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.minh.testservice.enums.TypePart;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class Part {
    @Field
    private int id;
    @Field
    private String name;
    @Field
    private List<Question> questions=new ArrayList<>();
    @Field
    private List<QuestionGroup> questionGroups=new ArrayList<>();
    @Field
    private double scorePerQuestion;
    @Field
    private TypePart typePart;
}
