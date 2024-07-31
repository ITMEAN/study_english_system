package org.minh.testservice.service.calcScoreService;

import lombok.RequiredArgsConstructor;
import org.minh.testservice.entity.AnswerQuestion;
import org.minh.testservice.entity.ResultTest;
import org.minh.testservice.entity.Test;
import org.minh.testservice.enums.StatusAnswer;
import org.minh.testservice.enums.TypePart;
import org.minh.testservice.model.dto.AnswerQuestionDTO;
import org.minh.testservice.service.test.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CalcScoreToeicServiceImpl  implements CalcScoreService {
    @Autowired
    private final TestService testService;
    private final double READING_SCORE = 495;
    private final double LISTENING_SCORE = 495;
    private final double TOTAL_SCORE = 990;
    private final double READING_SCORE_PER_QUESTION = 5;

    @Override
    public ResultTest getResultTest(Test test, List<AnswerQuestion> answerQuestions) {
        int totalQuestion = getTotalQuestion(test);
        double totalScore = getTotalScore(test, answerQuestions);
        int totalSkipQuestion = getTotalSkipQuestion(test, answerQuestions);
        int totalCorrectQuestion = getTotalCorrectQuestion(test, answerQuestions);
        int totalWrongQuestion = getTotalWrongQuestion(test, answerQuestions);
        int totalQuestionListening = getNumberQuestionByType(test, answerQuestions, TypePart.LISTENING.name());
        int totalQuestionReading = getNumberQuestionByType(test, answerQuestions, TypePart.READING.name());
        int totalCorrectQuestionListening = getNumberQuestionCorrectByType(test, answerQuestions, TypePart.LISTENING.name());
        int totalCorrectQuestionReading = getNumberQuestionCorrectByType(test, answerQuestions, TypePart.READING.name());
        double scoreListening = getScoreByType(test, answerQuestions, TypePart.LISTENING.name());
        double scoreReading = getScoreByType(test, answerQuestions, TypePart.READING.name());
        double resultScoreListening = getResultScoreByType(test, answerQuestions, TypePart.LISTENING.name());
        double resultScoreReading = getResultScoreByType(test, answerQuestions, TypePart.READING.name());
        double accuracy = (totalCorrectQuestion * 1.0 / totalQuestion) * 100;
        return ResultTest.builder()
                .totalQuestion(totalQuestion)
                .totalScore(totalScore)
                .totalSkip(totalSkipQuestion)
                .totalCorrect(totalCorrectQuestion)
                .totalIncorrect(totalWrongQuestion)
                .totalQuestionListening(totalQuestionListening)
                .totalQuestionReading(totalQuestionReading)
                .totalScoreListening(scoreListening)
                .totalScoreReading(scoreReading)
                .numberQuestionCorrectListening(totalCorrectQuestionListening)
                .numberQuestionCorrectReading(totalCorrectQuestionReading)
                .resultScoreListening(resultScoreListening)
                .resultScoreReading(resultScoreReading)
                .accuracy(accuracy)
                .build();
    }

    @Override
    public int getTotalQuestion(Test test) {
        return test.getNumberQuestion();
    }

    @Override
    public double getTotalScore(Test test, List<AnswerQuestion> answerQuestionDTOList) {
        return getResultScoreByType(test, answerQuestionDTOList, TypePart.LISTENING.name()) + getResultScoreByType(test, answerQuestionDTOList, TypePart.READING.name());
    }

    @Override
    public int getTotalSkipQuestion(Test test, List<AnswerQuestion> answerQuestionDTOList) {
        return (int) answerQuestionDTOList.stream().filter(answerQuestion -> answerQuestion.getStatusAnswer() == StatusAnswer.SKIP).count();
    }


    @Override
    public int getTotalCorrectQuestion(Test test, List<AnswerQuestion> answerQuestions) {
        return (int) answerQuestions.stream().filter(answerQuestion -> answerQuestion.getStatusAnswer() == StatusAnswer.CORRECT).count();
    }

    @Override
    public int getTotalWrongQuestion(Test test, List<AnswerQuestion> answerQuestions) {
        return (int) answerQuestions.stream().filter(answerQuestion -> answerQuestion.getStatusAnswer() == StatusAnswer.WRONG).count();
    }

    @Override
    public int getNumberQuestionByType(Test test, List<AnswerQuestion> answerQuestionDTOS, String type) {
        return (int) answerQuestionDTOS.stream().filter(answerQuestion -> testService.findPartById(test, answerQuestion.getIdPart(), "calc").getTypePart().equals(TypePart.valueOf(type))).count();
    }

    @Override
    public int getNumberQuestionCorrectByType(Test test, List<AnswerQuestion> answerQuestions, String type) {
        return answerQuestions.stream().filter(answerQuestion -> testService.findPartById(test, answerQuestion.getIdPart(), "calc").getTypePart().equals(TypePart.valueOf(type)))
                .mapToInt(answerQuestion -> answerQuestion.getStatusAnswer() == StatusAnswer.CORRECT ? 1 : 0)
                .sum();
    }

    @Override
    public double getScoreByType(Test test, List<AnswerQuestion> answerQuestionDTOS, String type) {
        return switch (type) {
            case "READING" -> READING_SCORE;
            case "LISTENING" -> LISTENING_SCORE;
            default -> 495;
        };
    }

    @Override
    public double getResultScoreByType(Test test, List<AnswerQuestion> answerQuestionDTOS, String type) {
        int totalQuestionCorrectListening = getNumberQuestionCorrectByType(test, answerQuestionDTOS, type);
        if (type.equals(TypePart.LISTENING.name())) {
            return calculateListeningScore(totalQuestionCorrectListening);
        } else if (type.equals(TypePart.READING.name())) {
            return calculateReadingScore(totalQuestionCorrectListening);
        } else {
            return 0;
        }
    }

    private double calculateListeningScore(int totalQuestionCorrectListening) {
        if (totalQuestionCorrectListening == 0) {
            return 0;
        } else if (totalQuestionCorrectListening >= 1 && totalQuestionCorrectListening < 96) {
            return totalQuestionCorrectListening * READING_SCORE_PER_QUESTION + 10;
        } else {
            return LISTENING_SCORE;
        }
    }

    private double calculateReadingScore(int totalQuestionCorrectReading) {
        if (totalQuestionCorrectReading == 0) {
            return 0;
        } else if (totalQuestionCorrectReading == 1 || totalQuestionCorrectReading == 2) {
            return 5;
        } else {
            return totalQuestionCorrectReading * READING_SCORE_PER_QUESTION - 5;
        }
    }


}
