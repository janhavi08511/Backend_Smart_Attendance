package com.example.smart_attendance.service;

import com.example.smart_attendance.dto.requests.RegisterTeacherRequest;
import com.example.smart_attendance.dto.requests.TeacherLoginRequest;
import com.example.smart_attendance.model.Teacher;
import com.example.smart_attendance.repository.TeacherRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public TeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    public Teacher register(RegisterTeacherRequest req) {
        if (!req.password.equals(req.confirmPassword)) {
            throw new IllegalArgumentException("Passwords do not match");
        }
        if (teacherRepository.existsByEmail(req.email)) throw new IllegalArgumentException("Email already used");
        if (teacherRepository.existsByTeacherId(req.teacherId)) throw new IllegalArgumentException("Teacher ID already used");

        Teacher t = new Teacher(
                req.name,
                req.department,
                req.email,
                encoder.encode(req.password),
                req.teacherId,
                req.profilePicUrl
        );
        return teacherRepository.save(t);
    }

    public Optional<Teacher> login(TeacherLoginRequest req) {
        Optional<Teacher> teacher = req.emailOrTeacherId.contains("@")
                ? teacherRepository.findByEmail(req.emailOrTeacherId)
                : teacherRepository.findByTeacherId(req.emailOrTeacherId);

        if (teacher.isPresent() && encoder.matches(req.password, teacher.get().getPassword())) {
            return teacher;
        }
        return Optional.empty();
    }

    public long countTeachers() {
        return teacherRepository.count();
    }

    public Optional<Teacher> findByTeacherId(String teacherId) {
        return teacherRepository.findByTeacherId(teacherId);
    }
}
