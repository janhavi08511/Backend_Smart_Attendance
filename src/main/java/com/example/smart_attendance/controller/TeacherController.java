package com.example.smart_attendance.controller;

import com.example.smart_attendance.dto.MessageResponse;
import com.example.smart_attendance.dto.requests.RegisterTeacherRequest;
import com.example.smart_attendance.dto.requests.TeacherLoginRequest;
import com.example.smart_attendance.model.Teacher;
import com.example.smart_attendance.service.TeacherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional; // 🔹 Import Optional

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/teacher")
public class TeacherController {

    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
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
                .<ResponseEntity<?>>map(t -> ResponseEntity.ok(Map.of(
                        "message", "Login successful",
                        "teacherId", t.getTeacherId(),
                        // TODO: return JWT token if using JWT; for now we return teacherId
                        "token", "dummy-token"
                )))
                .orElse(ResponseEntity.status(401).body(new MessageResponse("Invalid credentials")));
    }

    /**
     * 🔹 New endpoint to fetch teacher profile details.
     * This is called by the dashboard after a successful login.
     */
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