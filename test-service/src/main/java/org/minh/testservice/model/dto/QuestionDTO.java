package org.minh.testservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionDTO {
    private String description;
    private List<OptionDTO> options;
    private int answerId;
    private boolean isHideQuestion;
    private boolean isHideOption;
    private int choose;
}
