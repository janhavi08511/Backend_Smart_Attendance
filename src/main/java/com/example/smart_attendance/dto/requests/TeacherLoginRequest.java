package com.example.smart_attendance.dto.requests;

public class TeacherLoginRequest {
    public String emailOrTeacherId;
    public String password; // Changed from passwordHash
}