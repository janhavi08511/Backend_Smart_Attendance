package com.example.smart_attendance.service;

import com.example.smart_attendance.dto.requests.CreateAttendenceRequest;
import com.example.smart_attendance.dto.requests.FinalizeAttendenceRequest;
import com.example.smart_attendance.dto.requests.MarkattendenceRequest;
import com.example.smart_attendance.model.AttendenceRecord;
import com.example.smart_attendance.model.PresentStudent;
import com.example.smart_attendance.model.Student;
import com.example.smart_attendance.repository.AttendenceRepository;
import com.example.smart_attendance.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AttendenceService {

    private final AttendenceRepository attendenceRepository;
    private final StudentRepository studentRepository;
    private final StudentService studentService;

    public AttendenceService(AttendenceRepository attendenceRepository,
                             StudentRepository studentRepository,
                             StudentService studentService) {
        this.attendenceRepository = attendenceRepository;
        this.studentRepository = studentRepository;
        this.studentService = studentService;
    }

    public AttendenceRecord createAttendance(String teacherId, CreateAttendenceRequest req) {
        AttendenceRecord record = new AttendenceRecord();
        record.setTeacherId(teacherId);
        record.setClassName(req.className());
        record.setSubject(req.subject());
        record.setRoomNumber(req.roomNumber());
        record.setDepartment(req.department());
        record.setCreatedAt(Instant.now());
        record.setPresentStudents(new ArrayList<>());
        return attendenceRepository.save(record);
    }

    public AttendenceRecord finalizeWithBeacon(FinalizeAttendenceRequest req) {
        AttendenceRecord record = attendenceRepository.findById(req.attendanceId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid attendanceId: " + req.attendanceId()));

        // ✅ Use the beacon UUID from the request
        record.setBeaconUuid(req.beaconUuid());
        Instant now = Instant.now();
        record.setBeaconActivatedAt(now);
        record.setBeaconDeactivatedAt(now.plus(3, ChronoUnit.MINUTES));

        return attendenceRepository.save(record);
    }

    public void markAttendenceForStudent(MarkattendenceRequest req) {
        AttendenceRecord record = attendenceRepository.findById(req.attendanceId())
                .orElseThrow(() -> new IllegalArgumentException("Attendance session not found."));

        // ✅ Add validation
        if (record.getBeaconDeactivatedAt() == null || Instant.now().isAfter(record.getBeaconDeactivatedAt())) {
            throw new IllegalArgumentException("Attendance session is not active.");
        }
        if (!record.getBeaconUuid().equalsIgnoreCase(req.beaconUuid())) {
            throw new IllegalArgumentException("Beacon UUID does not match.");
        }

        Student student = studentService.ensureStudentExists(req.crn(), req.name(), req.rollNo());

        boolean isAlreadyMarked = record.getPresentStudents().stream()
                .anyMatch(p -> p.getRollNo().equals(student.getRollNo()) || p.getDeviceId().equals(req.deviceId()));

        if (isAlreadyMarked) {
            throw new IllegalArgumentException("Attendance already marked for this student or device.");
        }

        PresentStudent presentStudent = new PresentStudent(student.getId(), student.getName(), student.getRollNo(), Instant.now());
        presentStudent.setDeviceId(req.deviceId());
        record.getPresentStudents().add(presentStudent);
        attendenceRepository.save(record);
    }

    public AttendenceRecord stopBeaconSession(String attendanceId) {
        AttendenceRecord record = attendenceRepository.findById(attendanceId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid attendanceId: " + attendanceId));
        record.setBeaconDeactivatedAt(Instant.now());
        return attendenceRepository.save(record);
    }

    public Optional<AttendenceRecord> findActiveByBeacon(String beaconUuid) {
        return attendenceRepository.findByBeaconUuidAndBeaconDeactivatedAtAfter(beaconUuid, Instant.now())
                .stream()
                .max(Comparator.comparing(AttendenceRecord::getBeaconActivatedAt));
    }

    public Map<String, Object> getAttendanceSummary(String attendanceId) {
        AttendenceRecord record = attendenceRepository.findById(attendanceId)
                .orElseThrow(() -> new RuntimeException("Attendance record not found for ID: " + attendanceId));

        List<String> presentRollNos = record.getPresentStudents().stream()
                .map(PresentStudent::getRollNo)
                .collect(Collectors.toList());

        List<Student> allStudents = studentRepository.findByCrn(record.getClassName());

        List<Student> absentStudents = allStudents.stream()
                .filter(student -> !presentRollNos.contains(student.getRollNo()))
                .collect(Collectors.toList());

        Map<String, Object> summary = new HashMap<>();
        summary.put("presentCount", presentRollNos.size());
        summary.put("absentCount", absentStudents.size());
        summary.put("presentStudents", record.getPresentStudents());
        summary.put("absentStudents", absentStudents);

        return summary;
    }

    public boolean isBeaconActive(String attendanceId) {
        return attendenceRepository.findById(attendanceId)
                .map(r -> r.getBeaconDeactivatedAt() != null && Instant.now().isBefore(r.getBeaconDeactivatedAt()))
                .orElse(false);
    }
}