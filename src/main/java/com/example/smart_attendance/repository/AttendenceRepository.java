package com.example.smart_attendance.repository;

import com.example.smart_attendance.model.AttendenceRecord;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface AttendenceRepository extends MongoRepository<AttendenceRecord, String> {
    List<AttendenceRecord> findByClassroomId(String classroomId);
    Optional<AttendenceRecord> findByTeacherIdAndClassNameAndSubjectAndCreatedAt(
            String teacherId, String className, String subject, Instant createdAt);

    List<AttendenceRecord> findByBeaconUuidAndBeaconDeactivatedAtAfter(String beaconUuid, Instant now);
}

