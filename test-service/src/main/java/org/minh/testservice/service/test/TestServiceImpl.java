package org.minh.testservice.service.test;

import lombok.RequiredArgsConstructor;
import org.minh.testservice.entity.*;
import org.minh.testservice.enums.TypePart;
import org.minh.testservice.enums.TypeTest;
import org.minh.testservice.exception.DataNotFoundException;
import org.minh.testservice.exception.S3UploadException;
import org.minh.testservice.model.dto.QuestionDTO;
import org.minh.testservice.model.request.test.AddGroupQuestion;
import org.minh.testservice.model.request.test.AddPartToTestRequest;
import org.minh.testservice.model.request.test.AddQuestionToPartRequest;
import org.minh.testservice.model.request.test.AddTestRequest;
import org.minh.testservice.model.response.ListTestResponse;
import org.minh.testservice.repositories.TestRepository;
import org.minh.testservice.service.s3upload.AmazonS3UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {
    @Autowired
    private final TestRepository testRepository;
    @Autowired
    private final AmazonS3UploadService amazonS3UploadService;


    @Override
    public ListTestResponse getTests(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Test> pageTest = testRepository.findAll(pageable);
        return ListTestResponse.builder().tests(pageTest.getContent()).total(pageTest.getTotalPages()).build();
    }

    @Override
    public Test addTest(AddTestRequest request) throws S3UploadException {
        String mp3 = amazonS3UploadService.uploadFile(request.getMp3());
        try {
            Test test = Test.builder()
                    .mp3(mp3)
                    .name(request.getName())
                    .time(request.getTime())
                    .typeTest(TypeTest.TOEIC)
                    .createDate(LocalDate.now())
                    .active(false)
                    .description(request.getDescription())
                    .build();
            return testRepository.save(test);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }


    }

    @Override
    public Test addPartToTest(AddPartToTestRequest request) throws S3UploadException {
        Test test = testRepository.findById(request.getTestId()).orElseThrow(() -> new DataNotFoundException("Test not found"));
        int sizePart = test.getParts().size();
        int nextIdPart = sizePart + 1;
        String typePart = "";
        try {
            typePart = TypePart.valueOf(request.getTypePart()).name();
        } catch (Exception e) {
            throw new DataNotFoundException("Type part not found");
        }
        Part part = Part.builder()
                .id(nextIdPart)
                .name(request.getName())
                .scorePerQuestion(request.getScorePerQuestion())
                .typePart(TypePart.valueOf(typePart))
                .build();
        //
        test.setActive(true);
        test.setNumberPart(test.getNumberPart() + 1);
        test.getParts().add(part);
        return testRepository.save(test);
    }

    @Override
    public Test addQuestionToPart(AddQuestionToPartRequest request) throws S3UploadException {
        Test test = testRepository.findById(request.getTestId()).orElseThrow(() -> new DataNotFoundException("Test not found"));
        Part part = test.getParts().stream().filter(p -> p.getId() == request.getPartId()).findFirst().orElseThrow(() -> new DataNotFoundException("Part not found"));
        int nextId = test.getNumberQuestion() + 1;
        String urlImage = "";
        if (request.getImage() != null) {
            urlImage = amazonS3UploadService.uploadFile(request.getImage());
        }
        System.out.println(request.getImage());
        List<Option> options = request.getOptions()
                .stream()
                .map(optionDTO -> Option
                        .builder()
                        .id(optionDTO.getId())
                        .content(optionDTO.getContent())
                        .name(Option.generateNameById(optionDTO.getId()))
                        .build())
                .toList();
        Question question = Question
                .builder()
                .isHideQuestion(request.isHideQuestion())
                .image(urlImage)
                .answerId(request.getAnswerId())
                .isHideOption(request.isHideOption())
                .options(options)
                .description(request.getDescription())
                .id(nextId)
                .build();
        part.getQuestions().add(question);
        test.setParts(test.getParts().stream().map(p -> p.getId() == request.getPartId() ? part : p
        ).toList());
        test.setNumberQuestion(test.getNumberQuestion() + 1);
        return testRepository.save(test);

    }

    @Override
    public Test addGroupQuestionToPart(AddGroupQuestion request) throws S3UploadException {
        //find test
        Test test = testRepository.findById(request.getTestId()).orElseThrow(() -> new DataNotFoundException("Test not found"));
        //find part
        Part part = test.getParts().stream()
                .filter(p -> p.getId() == request.getPartId())
                .findFirst()
                .orElseThrow(() -> new DataNotFoundException("Part not found"));
        //upload image
        String urlImage = amazonS3UploadService.uploadFile(request.getImage());
        //get nextId
        int nextId = test.getNumberQuestion() + 1;
        List<Question> questions = new ArrayList<>();
        for (QuestionDTO questionDTO : request.getQuestions()) {
            Question question = Question.builder().id(nextId)
                    .isHideOption(questionDTO.isHideOption())
                    .isHideQuestion(questionDTO.isHideQuestion())
                    .answerId(questionDTO.getAnswerId())
                    .description(questionDTO.getDescription())
                    .options(questionDTO.getOptions()
                            .stream()
                            .map(optionDTO -> Option
                                    .builder()
                                    .id(optionDTO.getId())
                                    .content(optionDTO.getContent())
                                    .name(Option.generateNameById(optionDTO.getId()))
                                    .build())
                            .toList()).build();
            questions.add(question);
            nextId++;
        }
        QuestionGroup questionGroup = QuestionGroup.builder().questions(questions).image(urlImage).build();
        //add question group
        part.getQuestionGroups().add(questionGroup);
        //update test
        test.setParts(test.getParts()
                .stream()
                .map(p -> p.getId() == part.getId() ? part : p
                ).toList());
        test.setNumberQuestion(test.getNumberQuestion() + questions.size());
        return testRepository.save(test);


    }


    @Override
    public Test getTestById(String id) {
        return testRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Test not found"));
    }

    @Override
    public Test deleteTest(String id) {
        return null;
    }

    @Override
    public Test deletePart() {
        return null;
    }

    @Override
    public Part findPartById(Test test, int idPart, String action) {
        return test.getParts().stream().filter(p -> p.getId() == idPart).findFirst().orElseThrow(
                () -> new DataNotFoundException("part not found: " + idPart + " action: " + action)
        );
    }

    @Override
    public Question findQuestionById(Part part, int idQuestion) {
        return part.getQuestions().stream().filter(p -> p.getId() == idQuestion)
                .findFirst()
                .or(() -> findQuestionInComboQuestion(idQuestion, part.getQuestionGroups())).orElseThrow(
                        () -> new DataNotFoundException("question not found:" + idQuestion)
                );
    }

    @Override
    public Optional<Question> findQuestionInComboQuestion(int idQuestion, List<QuestionGroup> questionGroups) {
           return questionGroups
                .stream()
                .flatMap(questionGroup -> questionGroup.getQuestions().stream())
                .toList()
                .stream().filter(question -> question.getId() == idQuestion).findFirst();
    }
}
