package com.project.fitness.controller;

import com.project.fitness.dto.Register.ActivityRequest;
import com.project.fitness.dto.Register.ActivityResponse;
import com.project.fitness.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/activities")
public class ActivityController {
    private final ActivityService activityService;

    @PostMapping
    public ResponseEntity<ActivityResponse> trackActivity(@RequestBody ActivityRequest request) {
        return ResponseEntity.ok(activityService.traceActivity(request));
    }


    @GetMapping
    public ResponseEntity<List<ActivityResponse>> getUserActivities(
            @RequestHeader(value = "x-User-Id") String userId
    ) {
        return ResponseEntity.ok(activityService.getUserActivities(userId));
    }
}


