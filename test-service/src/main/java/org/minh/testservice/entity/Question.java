package org.minh.testservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class Question {
    private int id;
    private String description;
    private List<Option> options = new ArrayList<>();
    private String image;
    private int answerId;
    private boolean isHideQuestion;
    private boolean isHideOption;
}
