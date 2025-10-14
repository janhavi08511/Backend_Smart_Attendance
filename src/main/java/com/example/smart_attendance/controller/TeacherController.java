package com.example.smart_attendance.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.smart_attendance.dto.MessageResponse;
import com.example.smart_attendance.dto.requests.RegisterTeacherRequest;
import com.example.smart_attendance.dto.requests.TeacherLoginRequest;
import com.example.smart_attendance.model.Teacher;
import com.example.smart_attendance.service.JwtService;
import com.example.smart_attendance.service.TeacherService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/teacher")
public class TeacherController {

    private final TeacherService teacherService;
    private final JwtService jwtService;

    // ✅ FIXED: Correctly inject both services
    public TeacherController(TeacherService teacherService, JwtService jwtService) {
        this.teacherService = teacherService;
        this.jwtService = jwtService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody RegisterTeacherRequest req) {
        Teacher t = teacherService.register(req);
        return ResponseEntity.ok(Map.of(
                "id", t.getId(),
                "email", t.getEmail(),
                "teacherId", t.getTeacherId(),
                "message", "Profile created. Please login."
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody TeacherLoginRequest req) {
        return teacherService.login(req)
                .map(teacher -> {
                    UserDetails userDetails = User.builder()
                            .username(teacher.getTeacherId())
                            .password(teacher.getPasswordHash())
                            .roles("TEACHER")
                            .build();
                    
                    String jwtToken = jwtService.generateToken(userDetails);

                    return ResponseEntity.ok(Map.of(
                            "message", "Login successful",
                            "teacherId", teacher.getTeacherId(),
                            "token", jwtToken
                    ));
                })
                // ✅ FIXED: Return a Map in the error case to match the success case
                .orElse(ResponseEntity.status(401).body(Map.of("error", "Invalid credentials")));
    }

    @GetMapping("/profile/{teacherId}")
    public ResponseEntity<?> getProfile(@PathVariable String teacherId) {
        Optional<Teacher> teacherOpt = teacherService.findByTeacherId(teacherId);
        return teacherOpt
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).body(new MessageResponse("Teacher not found")));
    }

    @GetMapping("/count")
    public ResponseEntity<?> count() {
        return ResponseEntity.ok(Map.of("total", teacherService.countTeachers()));
    }
}