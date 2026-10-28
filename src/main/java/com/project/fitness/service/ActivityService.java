package com.project.fitness.service;

import com.project.fitness.dto.Register.ActivityRequest;
import com.project.fitness.dto.Register.ActivityResponse;
import com.project.fitness.model.Activity;
import com.project.fitness.model.User;
import com.project.fitness.repository.ActivityRepository;
import com.project.fitness.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service

@RequiredArgsConstructor
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;

    public ActivityResponse traceActivity(ActivityRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(()-> new RuntimeException("Invalid user:"+request.getUserId()));

        Activity activity=Activity.builder()
                .user(user)
                .type(request.getType())
                .totalDays(request.getTotalDays())
                .caloriesBurned(request.getCaloriesBurned())
                .startTime(request.getStartTime())
                .additionalMetrics(request.getAdditionalMetrics())
                .build();

            Activity savedActivity =    activityRepository.save(activity);
                                        return mapToResponse(savedActivity);
    }

    private ActivityResponse mapToResponse(Activity activity) {

        ActivityResponse response = new ActivityResponse();

        response.setId(activity.getId());
        response.setUserId(activity.getUser().getId());
        response.setType(activity.getType());
        response.setTotalDays(activity.getTotalDays());
        response.setCaloriesBurned(activity.getCaloriesBurned());
        response.setStartTime(activity.getStartTime());
        response.setAdditionalMetrics(activity.getAdditionalMetrics());
        response.setCreatedAt(activity.getCreatedAt());
        response.setUpdatedAt(activity.getUpdatedAt());

        return response;

    }

    public List<ActivityResponse> getUserActivities(String userId) {
        List<Activity>activityList=activityRepository.findByUserId(userId);
        return activityList.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    }


