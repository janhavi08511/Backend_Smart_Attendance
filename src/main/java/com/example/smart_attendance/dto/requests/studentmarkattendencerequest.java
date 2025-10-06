package com.example.smart_attendance.dto.requests;

import jakarta.validation.constraints.NotBlank;

// ✅ NEW: A dedicated object for this request for better validation and clarity.
public record studentmarkattendencerequest(
        @NotBlank String crn,
        @NotBlank String name,
        @NotBlank String rollNo,
        @NotBlank String attendanceId,
        @NotBlank String deviceId
) {}