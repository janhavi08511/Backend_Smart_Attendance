package com.example.smart_attendance.controller;

import com.example.smart_attendance.dto.MessageResponse;
import com.example.smart_attendance.dto.requests.RegisterTeacherRequest;
import com.example.smart_attendance.dto.requests.TeacherLoginRequest;
import com.example.smart_attendance.model.Teacher;
import com.example.smart_attendance.service.JwtService;
import com.example.smart_attendance.service.TeacherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/teacher")
public class TeacherController {

    private final TeacherService teacherService;
    private final JwtService jwtService;

    public TeacherController(TeacherService teacherService, JwtService jwtService) {
        this.teacherService = teacherService;
        this.jwtService = jwtService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody RegisterTeacherRequest req) {
        try {
            Teacher t = teacherService.register(req);
            Map<String, Object> responseBody = Map.of(
                    "message", "Profile created successfully. Please login.",
                    "teacherId", t.getTeacherId()
            );
            return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Error: " + e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody TeacherLoginRequest req) {
        // ✅ FIXED: Refactored to use an if-else block to resolve the type inference error.
        Optional<Teacher> teacherOpt = teacherService.login(req);

        if (teacherOpt.isPresent()) {
            Teacher teacher = teacherOpt.get();
            // Create UserDetails for JWT generation
            UserDetails userDetails = User.builder()
                    .username(teacher.getTeacherId())
                    .password(teacher.getPasswordHash()) // The password here is the stored hash
                    .roles("TEACHER")
                    .build();

            String jwtToken = jwtService.generateToken(userDetails);

            // More structured success response
            Map<String, Object> responseBody = Map.of(
                    "message", "Login successful",
                    "teacherId", teacher.getTeacherId(),
                    "token", jwtToken
            );
            return ResponseEntity.ok(responseBody);
        } else {
            // Standardized error response for consistency
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Error: Invalid credentials"));
        }
    }

    @GetMapping("/profile/{teacherId}")
    public ResponseEntity<?> getProfile(@PathVariable String teacherId) {
        // ✅ FIXED: Refactored to use an if-else block to resolve the type inference error.
        Optional<Teacher> teacherOpt = teacherService.findByTeacherId(teacherId);
        if (teacherOpt.isPresent()) {
            return ResponseEntity.ok(teacherOpt.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Error: Teacher not found"));
        }
    }

    @GetMapping("/count")
    public ResponseEntity<?> count() {
        return ResponseEntity.ok(Map.of("total", teacherService.countTeachers()));
    }
}

