package com.example.smart_attendance.dto.requests;

import jakarta.validation.constraints.NotBlank;

// This DTO is for the first step and does not contain the beacon UUID.
public record CreateAttendenceRequest(
        @NotBlank String teacherId,
        @NotBlank String department,
        @NotBlank String className,
        @NotBlank String subject,
        @NotBlank String roomNumber
) {}
