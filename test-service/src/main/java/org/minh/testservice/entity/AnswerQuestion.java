package org.minh.testservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.minh.testservice.enums.StatusAnswer;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnswerQuestion {
    private int idPart;
    private Question question;
    private int idAnswer;
    private StatusAnswer statusAnswer;

}
