package org.minh.testservice.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "result_test")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ResultTest {
    @Id
    @EqualsAndHashCode.Include
    private String id;
    @Field
    private String email;
    @Field
    private String idTest;
    @Field
    private String time;
    @Field
    private Date date;
    @Field
    private List<AnswerQuestion> answerQuestions;
    @Field
    private double totalScore;
    @Field
    private int totalCorrect;
    @Field
    private int totalSkip;
    @Field
    private int totalQuestion;
    @Field
    private int totalIncorrect;
    @Field
    private int totalPart;
    @Field long totalQuestionReading;
    @Field long totalQuestionListening;
    @Field
    private double resultScoreReading;
    @Field
    private double totalScoreReading;
    @Field
    private double resultScoreListening;
    @Field
    private double totalScoreListening;
    @Field
    private int numberQuestionCorrectListening;
    @Field
    private int numberQuestionCorrectReading;
    @Field
    private double accuracy;



}
