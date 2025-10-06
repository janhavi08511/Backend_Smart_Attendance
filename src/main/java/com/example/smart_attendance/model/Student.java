package com.example.smart_attendance.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "students")

public class Student {

    @Id
    private String id;

    private String crn;          // College Registration Number
    private String name;         // Full name
    private String rollNo;       // Roll number
    private String role = "STUDENT"; // fixed, cannot change

    private boolean blocked = false;
    private Instant blockedUntil;

    private Instant lastMarkedAt;  // <-- Add this

    public Student() {}

    public Student(String crn, String name, String rollNo) {
        this.crn = crn;
        this.name = name;
        this.rollNo = rollNo;
    }

    // --- getters and setters ---
    public String getId() { return id; }

    public String getCrn() { return crn; }
    public void setCrn(String crn) { this.crn = crn; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRollNo() { return rollNo; }
    public void setRollNo(String rollNo) { this.rollNo = rollNo; }

    public String getRole() { return role; }

    public boolean isBlocked() { return blocked; }
    public void setBlocked(boolean blocked) { this.blocked = blocked; }

    public Instant getBlockedUntil() { return blockedUntil; }
    public void setBlockedUntil(Instant blockedUntil) { this.blockedUntil = blockedUntil; }

    public Instant getLastMarkedAt() { return lastMarkedAt; }   // ✅ Fix
    public void setLastMarkedAt(Instant lastMarkedAt) { this.lastMarkedAt = lastMarkedAt; }  // ✅ Fix
}
