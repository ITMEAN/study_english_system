package org.minh.flashcardservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ResponseData {
    private String message;
    private Object data;
    private int status;
    private String timestamp;

}
