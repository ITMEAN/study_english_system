package org.minh.testservice.service.test;

import org.minh.testservice.entity.Part;
import org.minh.testservice.entity.Question;
import org.minh.testservice.entity.QuestionGroup;
import org.minh.testservice.entity.Test;
import org.minh.testservice.exception.S3UploadException;
import org.minh.testservice.model.request.test.AddGroupQuestion;
import org.minh.testservice.model.request.test.AddPartToTestRequest;
import org.minh.testservice.model.request.test.AddQuestionToPartRequest;
import org.minh.testservice.model.request.test.AddTestRequest;
import org.minh.testservice.model.response.ListTestResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TestService {
    ListTestResponse getTests(int page ,int size);
    Test addTest(AddTestRequest test) throws S3UploadException;
    Test addPartToTest(AddPartToTestRequest request) throws S3UploadException;
    Test addQuestionToPart(AddQuestionToPartRequest request) throws S3UploadException;
    Test addGroupQuestionToPart(AddGroupQuestion request) throws S3UploadException;
    Test getTestById(String id);
    Test deleteTest(String id);
    Test deletePart();
    Part findPartById(Test test, int idPart, String action);
    Question findQuestionById(Part part, int idQuestion);
    Optional<Question> findQuestionInComboQuestion(int idQuestion, List<QuestionGroup> questionGroups);
}
