package org.minh.testservice.service.calcScoreService;

import org.minh.testservice.entity.AnswerQuestion;
import org.minh.testservice.entity.ResultTest;
import org.minh.testservice.entity.Test;
import org.minh.testservice.model.dto.AnswerQuestionDTO;

import java.util.List;

public interface CalcScoreService {
    ResultTest getResultTest(Test test, List<AnswerQuestion> answerQuestions);
    int getTotalQuestion(Test test);
    double getTotalScore(Test test, List<AnswerQuestion> answerQuestionDTOList);

    int getTotalSkipQuestion(Test test, List<AnswerQuestion> answerQuestionDTOList);

    int getTotalCorrectQuestion(Test test, List<AnswerQuestion> answerQuestions);

    int getTotalWrongQuestion(Test test, List<AnswerQuestion> answerQuestions);

    default int getNumberQuestionByType(Test test, List<AnswerQuestion> answerQuestionDTOS,String type ){
        throw new UnsupportedOperationException("Not implemented");
    }

    default int getNumberQuestionCorrectByType(Test test, List<AnswerQuestion> answerQuestions,String type) {
        throw new UnsupportedOperationException("Not implemented");
    }

    default double getScoreByType(Test test, List<AnswerQuestion> answerQuestionDTOS,String type) {
        throw new UnsupportedOperationException("Not implemented");
    }
    default double getResultScoreByType(Test test, List<AnswerQuestion> answerQuestionDTOS,String type) {
        throw new UnsupportedOperationException("Not implemented");
    }


}
