package org.minh.testservice.model.request.test;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AddPartToTestRequest {
    private String name;
    private String testId;
    private double scorePerQuestion;
    private String typePart;

}
