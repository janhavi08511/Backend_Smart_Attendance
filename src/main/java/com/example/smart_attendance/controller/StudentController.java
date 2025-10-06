package com.example.smart_attendance.controller;

import jakarta.validation.Valid;
import com.example.smart_attendance.dto.requests.studentmarkattendencerequest; // ✅ Import the new DTO
import com.example.smart_attendance.model.Student;
import com.example.smart_attendance.service.StudentService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    // ✅ UPDATED: The endpoint now accepts a specific, validated request object.
    @PostMapping("/mark")
    public Map<String, String> markAttendance(@Valid @RequestBody studentmarkattendencerequest req) {

        // Create or get existing student profile
        Student student = service.ensureStudentExists(req.crn(), req.name(), req.rollNo());

        // Mark attendance using the student object and other details from the request
        String message = service.markAttendance(student, req.attendanceId(), req.deviceId());

        return Map.of(
                "attendanceId", req.attendanceId(),
                "rollNo", req.rollNo(),
                "message", message
        );
    }

}
