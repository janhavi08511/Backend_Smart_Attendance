package com.example.smart_attendance.model;

import java.time.Instant;

public class PresentStudent {
    private String studentId;
    private String rollNo;
    private Instant markedAt;
    private String deviceId;  // ✅ new field
    private String name;

    public PresentStudent() {}
    public PresentStudent(String studentId, String name, String rollNo, Instant markedAt) {
        this.studentId = studentId;
        this.name = name;
        this.rollNo = rollNo;
        this.markedAt = markedAt;
    }

    public PresentStudent(String id, String rollNo, Object o) {
        this.studentId = id;
        this.rollNo = rollNo;
        this.markedAt = Instant.now();
    }


    // --- Getters & Setters ---
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getRollNo() { return rollNo; }
    public void setRollNo(String rollNo) { this.rollNo = rollNo; }

    public Instant getMarkedAt() { return markedAt; }
    public void setMarkedAt(Instant markedAt) { this.markedAt = markedAt; }

    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
