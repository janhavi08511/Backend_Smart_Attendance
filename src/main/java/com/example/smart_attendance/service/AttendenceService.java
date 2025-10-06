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

    /**
     * Step 1: Creates the initial attendance record without activating a beacon.
     */
    public AttendenceRecord createAttendance(String teacherId, CreateAttendenceRequest req) {
        AttendenceRecord record = new AttendenceRecord();
        record.setTeacherId(teacherId);
        record.setClassName(req.className());
        record.setSubject(req.subject());
        record.setRoomNumber(req.roomNumber());
        record.setDepartment(req.department());
        record.setCreatedAt(Instant.now());
        record.setPresentStudents(new ArrayList<>());
        record.setCwStudents(new ArrayList<>());

        return attendenceRepository.save(record);
    }

    /**
     * Step 2: Finds an existing record and activates its beacon session.
     */
    public AttendenceRecord finalizeWithBeacon(FinalizeAttendenceRequest req) {
        AttendenceRecord record = attendenceRepository.findById(req.attendanceId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid attendanceId: " + req.attendanceId()));

        record.setBeaconUuid("FDA50693-A4E2-4FB1-AFCF-C6EB07647825");
        Instant now = Instant.now();
        record.setBeaconActivatedAt(now);
        record.setBeaconDeactivatedAt(now.plus(3, ChronoUnit.MINUTES));

        return attendenceRepository.save(record);
    }

    public void markAttendenceForStudent(MarkattendenceRequest req) {
        AttendenceRecord record = attendenceRepository.findById(req.attendanceId())
                .orElseThrow(() -> new IllegalArgumentException("Attendance session not found."));

        Student student = studentService.ensureStudentExists(req.crn(), req.name(), req.rollNo());

        if (record.getPresentStudents() != null && record.getPresentStudents().stream().anyMatch(p -> p.getRollNo().equals(student.getRollNo()))) {
            System.out.println("Student " + student.getName() + " already marked present.");
            return;
        }

        PresentStudent presentStudent = new PresentStudent(student.getName(), student.getRollNo(), Instant.now());

        if (record.getPresentStudents() == null) {
            record.setPresentStudents(new ArrayList<>());
        }
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

