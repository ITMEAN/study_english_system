package org.minh.testservice.model.request.test;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.minh.testservice.model.dto.OptionDTO;
import org.springframework.web.multipart.MultipartFile;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddQuestionToPartRequest implements Serializable {
    private int partId;
    private String testId;
    private String description;
    private List<OptionDTO> options;
    private MultipartFile image;
    private int answerId;
    private Boolean isHideQuestion;
    private Boolean isHideOption;

    public boolean isHideOption() {
        return this.isHideOption;
    }
    public boolean isHideQuestion() {
        return this.isHideQuestion;
    }
}
