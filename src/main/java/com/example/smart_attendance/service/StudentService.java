package com.example.smart_attendance.service;

import com.example.smart_attendance.model.Student;
import com.example.smart_attendance.repository.StudentRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }


    public Student ensureStudentExists(String crn, String name, String rollNo) {
        // ✅ FIX: Call findByRollNo on the 'studentRepository' instance variable
        Optional<Student> existingStudent = studentRepository.findByRollNo(rollNo);

        if (existingStudent.isPresent()) {
            return existingStudent.get();
        } else {
            Student newStudent = new Student();
            newStudent.setCrn(crn);
            newStudent.setName(name);
            newStudent.setRollNo(rollNo);
            return studentRepository.save(newStudent);
        }
    }

    public String markAttendance(Student student, String attendanceId, String deviceId) {
        System.out.println("Marking attendance for student " + student.getName() + " with device ID: " + deviceId);
        // TODO: Implement your logic to mark attendance using the AttendenceRepository.
        return "Attendance marked for " + student.getName();
    }
}