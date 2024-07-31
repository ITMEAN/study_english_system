package org.minh.testservice.service.calcScoreService;

import org.minh.testservice.entity.AnswerQuestion;
import org.minh.testservice.entity.ResultTest;
import org.minh.testservice.entity.Test;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CalcScoreCustomTestServiceImpl implements CalcScoreService {

    @Override
    public ResultTest getResultTest(Test test, List<AnswerQuestion> answerQuestions) {
        return null;
    }

    @Override
    public int getTotalQuestion(Test test) {
        return 0;
    }

    @Override
    public double getTotalScore(Test test, List<AnswerQuestion> answerQuestionDTOList) {
        return 0;
    }

    @Override
    public int getTotalSkipQuestion(Test test, List<AnswerQuestion> answerQuestionDTOList) {
        return 0;
    }

    @Override
    public int getTotalCorrectQuestion(Test test, List<AnswerQuestion> answerQuestions) {
        return 0;
    }

    @Override
    public int getTotalWrongQuestion(Test test, List<AnswerQuestion> answerQuestions) {
        return 0;
    }
}
