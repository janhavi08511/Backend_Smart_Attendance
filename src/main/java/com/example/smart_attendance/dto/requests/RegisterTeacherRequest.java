package com.example.smart_attendance.dto.requests;

public class RegisterTeacherRequest {
    public String name;
    public String department;
    public String email;
    public String password; // Changed from passwordHash
    public String confirmPassword;
    public String teacherId;
    public String profilePicUrl;
}