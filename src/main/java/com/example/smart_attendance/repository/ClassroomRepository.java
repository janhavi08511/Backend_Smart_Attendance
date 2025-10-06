package com.example.smart_attendance.repository;

import com.example.smart_attendance.model.Classroom;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ClassroomRepository extends MongoRepository<Classroom, String> {

    // ✅ The findAll() method is already provided by MongoRepository, so no changes are needed.

    Optional<Classroom> findByRoomNumber(String roomNumber);
    Optional<Classroom> findByBeaconUuid(String beaconUuid);
    Optional<Classroom> findByDepartmentAndClassNameAndSubject(String department, String className, String subject);
}