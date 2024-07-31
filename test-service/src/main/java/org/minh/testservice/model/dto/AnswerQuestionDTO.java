package org.minh.testservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.minh.testservice.entity.Question;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class AnswerQuestionDTO implements Serializable {
    private int idPart;
    private int idQuestion;
    private int idAnswer;
}
