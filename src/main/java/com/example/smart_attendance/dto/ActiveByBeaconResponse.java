package com.example.smart_attendance.dto;



public record ActiveByBeaconResponse(String attendanceId, String className, String subject, long activeUntilEpochMillis) {}

