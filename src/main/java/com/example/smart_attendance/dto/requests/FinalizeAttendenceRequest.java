package com.example.smart_attendance.dto.requests;

import jakarta.validation.constraints.NotBlank;

// This DTO is for the second step, providing the beacon UUID to activate the session.
public record FinalizeAttendenceRequest(
        @NotBlank String attendanceId,
        @NotBlank String beaconUuid
) {}

