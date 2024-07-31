package org.minh.testservice.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.minh.testservice.model.dto.AccuracyDTO;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnalyticsResponse {
    private int numberOfTest;
    private double totalTime;
    private int totalTryTest;
    private double avgAccuracy;
    private double avgTotalScore;
    private double bestTotalScore;
    private List<AccuracyDTO> accuracyByDate;

}
