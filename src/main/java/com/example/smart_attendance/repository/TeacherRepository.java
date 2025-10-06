package com.example.smart_attendance.repository;

import com.example.smart_attendance.model.Teacher;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TeacherRepository extends MongoRepository<Teacher, String> {
    Optional<Teacher> findByEmail(String email);
    Optional<Teacher> findByTeacherId(String teacherId);
    boolean existsByEmail(String email);
    boolean existsByTeacherId(String teacherId);
}
