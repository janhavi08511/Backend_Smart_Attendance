package com.example.smart_attendance.controller;

import com.example.smart_attendance.dto.ActiveByBeaconResponse;
import com.example.smart_attendance.dto.requests.CreateAttendenceRequest;
import com.example.smart_attendance.dto.requests.FinalizeAttendenceRequest;
import com.example.smart_attendance.dto.requests.MarkattendenceRequest;
import com.example.smart_attendance.model.AttendenceRecord;
import com.example.smart_attendance.service.AttendenceService;
import com.example.smart_attendance.service.BeaconService;
import com.example.smart_attendance.service.ExcelExportService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@Slf4j // ✅ Use Lombok for logging, which is cleaner
@RestController
@RequestMapping("/api/attendence")
public class AttendenceController {

    private final AttendenceService attendenceService;
    private final BeaconService beaconService;
    private final ExcelExportService excelExportService;

    // ✅ CORRECTED CONSTRUCTOR: It was missing parameters
    public AttendenceController(AttendenceService attendenceService, BeaconService beaconService, ExcelExportService excelExportService) {
        this.attendenceService = attendenceService;
        this.beaconService = beaconService;
        this.excelExportService = excelExportService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody CreateAttendenceRequest req) {
        try {
            AttendenceRecord r = attendenceService.createAttendance(req.teacherId(), req);
            return ResponseEntity.ok(Map.of(
                    "attendanceId", r.getId(),
                    "message", "Attendance record created. Please finalize to start the beacon."));
        } catch (Exception ex) {
            log.error("Error creating attendance", ex);
            return ResponseEntity.internalServerError().body(Map.of("error", ex.getMessage()));
        }
    }

    @PostMapping("/finalize")
    public ResponseEntity<?> finalizeWithBeacon(@Valid @RequestBody FinalizeAttendenceRequest req) {
        try {
            AttendenceRecord r = attendenceService.finalizeWithBeacon(req);
            // ❗ IMPORTANT: Replace with your actual port description from your PC's Device Manager
            beaconService.startBeacon("Your_ESP32_Port_Description_Here");
            return ResponseEntity.ok(Map.of(
                    "attendanceId", r.getId(),
                    "beaconUuid", r.getBeaconUuid(),
                    "activeUntil", r.getBeaconDeactivatedAt(),
                    "message", "Beacon activated for 3 minutes."));
        } catch (Exception ex) {
            log.error("Error finalizing attendance", ex);
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    @PostMapping("/mark")
    public ResponseEntity<?> markAttendance(@Valid @RequestBody MarkattendenceRequest req) {
        try {
            attendenceService.markAttendenceForStudent(req);
            return ResponseEntity.ok(Map.of(
                    "message", "Attendance marked successfully for roll number: " + req.rollNo()));
        } catch (Exception ex) {
            log.error("Error marking attendance", ex);
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    // ✅ NEW ENDPOINT FOR EXCEL EXPORT
    @GetMapping("/report/{attendanceId}/export")
    public ResponseEntity<byte[]> exportReport(@PathVariable String attendanceId) {
        try {
            // This logic now correctly fetches the record before exporting
            AttendenceRecord record = attendenceService.getAttendenceRecordById(attendanceId);
            byte[] excelFile = excelExportService.exportToExcel(record);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=attendence_report.xlsx")
                    .body(excelFile);
        } catch (Exception ex) {
            log.error("Error exporting report", ex);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/active-by-beacon")
    public ResponseEntity<?> activeByBeacon(@RequestParam String beaconUuid) {
        return attendenceService.findActiveByBeacon(beaconUuid)
                .<ResponseEntity<?>>map(r -> ResponseEntity.ok(new ActiveByBeaconResponse(
                        r.getId(), r.getClassName(), r.getSubject(), r.getBeaconDeactivatedAt().toEpochMilli())))
                .orElseGet(() -> ResponseEntity.status(404).body(Map.of("error", "No active session for this beacon")));
    }

    @PostMapping("/{attendanceId}/stop")
    public ResponseEntity<?> stopBeacon(@PathVariable String attendanceId) {
        try {
            attendenceService.stopBeaconSession(attendanceId);
            return ResponseEntity.ok(Map.of("message", "Beacon session stopped."));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    @GetMapping("/report/{attendanceId}")
    public ResponseEntity<?> getReport(@PathVariable String attendanceId) {
        try {
            return ResponseEntity.ok(attendenceService.getAttendanceSummary(attendanceId));
        } catch (Exception ex) {
            log.error("Error generating report", ex);
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    @GetMapping("/{attendanceId}/active")
    public ResponseEntity<?> isActive(@PathVariable String attendanceId) {
        return ResponseEntity.ok(Map.of("active", attendenceService.isBeaconActive(attendanceId)));
    }
}