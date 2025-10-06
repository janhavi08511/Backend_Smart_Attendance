package com.example.smart_attendance.dto.requests;

import jakarta.validation.constraints.NotBlank;

// This DTO is used by students to mark their attendance.
public record MarkattendenceRequest(
        @NotBlank String attendanceId,
        @NotBlank String crn,
        @NotBlank String name,
        @NotBlank String rollNo,
        @NotBlank String beaconUuid,
        @NotBlank String deviceId
) {}

