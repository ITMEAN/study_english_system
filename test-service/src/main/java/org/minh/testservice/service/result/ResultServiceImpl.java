package org.minh.testservice.service.result;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.core.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.minh.testservice.entity.*;
import org.minh.testservice.enums.StatusAnswer;
import org.minh.testservice.enums.TypePart;
import org.minh.testservice.exception.BadCredentialsException;
import org.minh.testservice.exception.DataNotFoundException;
import org.minh.testservice.model.dto.AccuracyDTO;
import org.minh.testservice.model.dto.AnswerQuestionDTO;
import org.minh.testservice.model.request.test.FindResultByIdRequest;
import org.minh.testservice.model.request.test.SubmitTestRequest;
import org.minh.testservice.model.response.AnalyticsResponse;
import org.minh.testservice.repositories.ResultRepository;
import org.minh.testservice.repositories.TestRepository;
import org.minh.testservice.service.calcScoreService.CalcScoreService;
import org.minh.testservice.service.jwt.JwtUtil;
import org.minh.testservice.service.test.TestService;
import org.minh.testservice.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.security.Security;
import java.text.DecimalFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ResultServiceImpl implements ResultService {
    @Autowired
    private TestRepository testRepository;
    @Autowired
    private ResultRepository resultRepository;
    @Autowired
    private TestService testService;
    @Autowired
    private JwtUtil util;
    @Qualifier("calcScoreToeicServiceImpl")
    @Autowired
    private CalcScoreService calcScoreServiceToeicServiceImpl;
    @Qualifier("calcScoreCustomTestServiceImpl")
    @Autowired
    private CalcScoreService calcScoreCustomTestServiceImpl;
    private final long TOTAL_TIME = 120;

    @Override
    public ResultTest submitTest(SubmitTestRequest submitTestRequest) {
        double totalScore = 0;
        //call user service by openfeign
        //find test
        Test test = testRepository.findById(submitTestRequest.getIdTest()).orElseThrow(() -> new DataNotFoundException("test not found"));
        List<AnswerQuestion> answerQuestions = new ArrayList<>();
        //calc score
        AnswerQuestion answerQuestion = null;
        for (AnswerQuestionDTO dto : submitTestRequest.getAnswerQuestions()) {
            Part part = testService.findPartById(test, dto.getIdPart(), "calc");
            Question question = testService.findQuestionById(part, dto.getIdQuestion());
            answerQuestion = AnswerQuestion.builder().idAnswer(dto.getIdAnswer()).question(question).idPart(dto.getIdPart()).build();
            if (question.getAnswerId() == dto.getIdAnswer()) {
                answerQuestion.setStatusAnswer(StatusAnswer.CORRECT);
            } else if (dto.getIdAnswer() == -1) {
                answerQuestion.setStatusAnswer(StatusAnswer.SKIP);
            } else {
                answerQuestion.setStatusAnswer(StatusAnswer.WRONG);
            }
            answerQuestions.add(answerQuestion);
        }
        ResultTest rs = calcResultTestByTypeTest(test.getTypeTest().name(), test, answerQuestions);
        rs.setAnswerQuestions(answerQuestions);
        rs.setEmail(submitTestRequest.getEmail());
        rs.setDate(Date.from(Instant.now()));
        rs.setTime(submitTestRequest.getTime());
        rs.setIdTest(test.getId());
        return resultRepository.save(rs);
    }

    @Override
    public ResultTest findResultTestById(String id, String token) throws BadCredentialsException {
        System.out.println(token);
        ResultTest resultTest = resultRepository.findById(id).orElseThrow(() -> new DataNotFoundException("result not found"));
        String email = util.getSubject(token);
        if (resultTest.getEmail().equals(email)) {
            return resultTest;
        } else {
            throw new BadCredentialsException("email not match");
        }
    }

    @Override
    public List<ResultTest> getResultTestByEmail(String email, String token) throws BadCredentialsException {
        String emailToken = util.getSubject(token.replace("Bearer ", ""));

        if (email.equals(emailToken)) {
            return resultRepository.findAllByEmailOrderByDateDesc(email);
        } else {
            throw new BadCredentialsException("email not match");
        }

    }

    @Override
    public AnalyticsResponse getAnalytics(String email, String token) throws BadCredentialsException {
        String emailToken = util.getSubject(token.replace("Bearer ", ""));
        DecimalFormat df = new DecimalFormat("#.00");
        if (email.equals(emailToken)) {
            List<ResultTest> resultTests = resultRepository.findAllByEmailOrderByDateDesc(email);
            //calc total
            int totalTest = resultTests.stream().distinct().toList().size();
            int totalTime = resultTests.stream().mapToInt(resultTests1 -> {
                return (int) (TOTAL_TIME * 60 - (int) TimeUtil.convertToMMSS(resultTests1.getTime()));
            }).sum();
            int totalTry = resultTests.size();
            double avgAccuracy = resultTests.stream().mapToDouble(ResultTest::getAccuracy).average().orElse(0);
            double avgTotalScore = resultTests.stream().mapToDouble(ResultTest::getTotalScore).average().orElse(0);
            avgAccuracy = Double.parseDouble(df.format(avgAccuracy));
            double bestScore = resultTests.stream().mapToDouble(ResultTest::getTotalScore).max().orElse(0);
            List<AccuracyDTO> accuracyByDate = resultTests.stream()
                    .collect(Collectors.groupingBy(ResultTest::getDate, Collectors.averagingDouble(ResultTest::getAccuracy))).entrySet().stream().map(
                            dateDoubleEntry -> AccuracyDTO.builder().date(dateDoubleEntry.getKey()).accuracy(Double.parseDouble(df.format(dateDoubleEntry.getValue()))).build()
                    ).toList();

            return AnalyticsResponse.builder().numberOfTest(totalTest).totalTime(totalTime).totalTryTest(totalTry).avgAccuracy(avgAccuracy).avgTotalScore(avgTotalScore).bestTotalScore(bestScore).accuracyByDate(accuracyByDate).build();

        } else {
            throw new BadCredentialsException("email not match");
        }

    }

    public ResultTest calcResultTestByTypeTest(String type, Test test, List<AnswerQuestion> answerQuestions) {
        return switch (type) {
            case "TOEIC" -> calcScoreServiceToeicServiceImpl.getResultTest(test, answerQuestions);
            case "CUSTOM" -> calcScoreCustomTestServiceImpl.getResultTest(test, answerQuestions);
            default -> null;
        };

    }


}

