package com.project.fitness.dto;

import com.project.fitness.dto.Register.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class loginResponse {
    private String token;
    private UserResponse user;
}
