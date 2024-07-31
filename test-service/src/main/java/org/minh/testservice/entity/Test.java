package org.minh.testservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.minh.testservice.enums.TypeTest;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Document(collection = "test")
public class Test {
    @Id
    private String id;
    @Field
    private String name;
    @Field
    private int year;
    @Field
    private String description;
    @Field
    private long time;
    @Field
    private TypeTest typeTest;
    @Field
    private String mp3;
    @Field
    private LocalDate createDate;
    @Field
    private int numberPart;
    @Field
    private int numberQuestion;
    @Field
    private List<Comment> comments = new ArrayList<>();
    @Field
    private List<Part> parts = new ArrayList<>();
    @Field
    private boolean active = false;
}
