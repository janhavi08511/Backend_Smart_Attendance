package com.example.smart_attendance.dto.requests;

import jakarta.validation.constraints.NotBlank;

// To support the fixed beacon workflow, the UUID is provided when creating a classroom.
public record RegisterClassroomRequest(
        @NotBlank String className,
        @NotBlank String subject,
        @NotBlank String roomNumber,
        @NotBlank String beaconUuid
) {}
