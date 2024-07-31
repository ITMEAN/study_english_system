package org.minh.testservice.model.request.test;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.minh.testservice.entity.Question;
import org.minh.testservice.model.dto.QuestionDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddGroupQuestion {
    private MultipartFile image;
    private int partId;
    private String testId;
    private List<QuestionDTO> questions;
}
