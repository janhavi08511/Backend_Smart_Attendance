package com.example.smart_attendance.dto.requests;

import jakarta.validation.constraints.NotBlank;

public record RegisterStudentRequest(
        @NotBlank String name,
        @NotBlank String rollNo,
        @NotBlank String department,
        String deviceId
) {}


