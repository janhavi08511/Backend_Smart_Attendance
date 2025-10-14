package com.example.smart_attendance.dto.requests;

public class RegisterTeacherRequest {
    public String name;
    public String department;
    public String email;
    public String password;
    public String confirmPassword;
    public String teacherId;
    public String profilePicUrl; // handle upload elsewhere, store URL here
}
