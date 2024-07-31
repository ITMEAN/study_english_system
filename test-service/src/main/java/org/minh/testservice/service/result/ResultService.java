package org.minh.testservice.service.result;

import org.minh.testservice.entity.ResultTest;
import org.minh.testservice.exception.BadCredentialsException;
import org.minh.testservice.model.request.test.FindResultByIdRequest;
import org.minh.testservice.model.request.test.SubmitTestRequest;
import org.minh.testservice.model.response.AnalyticsResponse;

import java.util.List;

public interface ResultService {
    ResultTest submitTest(SubmitTestRequest submitTestRequest);
    ResultTest findResultTestById(String id,String token) throws BadCredentialsException;
    List<ResultTest> getResultTestByEmail(String email, String token) throws BadCredentialsException;
    AnalyticsResponse getAnalytics(String email, String token) throws BadCredentialsException;
}
