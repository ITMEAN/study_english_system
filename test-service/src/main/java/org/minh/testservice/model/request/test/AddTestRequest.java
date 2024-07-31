package org.minh.testservice.model.request.test;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class AddTestRequest implements Serializable {
    private String name;
    private String description;
    private long time;
    private String typeTest;
    private MultipartFile mp3;
}
