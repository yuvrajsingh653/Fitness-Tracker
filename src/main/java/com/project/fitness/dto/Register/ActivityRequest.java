package com.project.fitness.dto.Register;

import com.project.fitness.model.ActivityType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityRequest {

    private String userId;
    private ActivityType type;
    private Map<String,Object> additionalMetrics;
    private Integer totalDays;
    private Integer caloriesBurned;
    private LocalDateTime startTime;

}
