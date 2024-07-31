package org.minh.testservice.model.request.test;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.minh.testservice.model.dto.AnswerQuestionDTO;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmitTestRequest implements Serializable {
    private String email;
    private String idTest;
    private String time;
    private List<AnswerQuestionDTO> answerQuestions;
}
