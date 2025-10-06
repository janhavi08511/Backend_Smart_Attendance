package com.example.smart_attendance.repository;

import com.example.smart_attendance.model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface StudentRepository extends MongoRepository<Student, String> {

    Optional<Student> findByRollNo(String rollNo);

    // Corrected to accept a String and return a List, as multiple students share a CRN (class name).
    List<Student> findByCrn(String crn);
}
