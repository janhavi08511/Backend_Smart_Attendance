package com.example.smart_attendance.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "attendence_records")
public class AttendenceRecord {
    @Id
    private String id;

    @Indexed
    private String classroomId;

    private String department;
    private String className;
    private String subject;
    private Instant dateTime;
    private String  RoomNumber;
    private String teacherId;
    private Instant createdAt;
    private String beaconUuid;
    private Instant beaconActivatedAt;
    private Instant beaconDeactivatedAt;
    // In AttendenceRecord.java
    private List<Student> cwStudents = new ArrayList<>();

    public List<Student> getCwStudents() { return cwStudents; }
    public void setCwStudents(List<Student> cwStudents) { this.cwStudents = cwStudents; }

    // ✅ Store full PresentStudent list
    private List<PresentStudent> presentStudents = new ArrayList<>();

    public AttendenceRecord() {}

    // --- getters & setters ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getClassroomId() { return classroomId; }
    public void setClassroomId(String classroomId) { this.classroomId = classroomId; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public Instant getDateTime() { return dateTime; }
    public void setDateTime(Instant dateTime) { this.dateTime = dateTime; }

    public String getTeacherId() { return teacherId; }
    public void setTeacherId(String teacherId) { this.teacherId = teacherId; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public String getBeaconUuid() { return beaconUuid; }
    public void setBeaconUuid(String beaconUuid) { this.beaconUuid = beaconUuid; }

    public Instant getBeaconActivatedAt() { return beaconActivatedAt; }
    public void setBeaconActivatedAt(Instant beaconActivatedAt) { this.beaconActivatedAt = beaconActivatedAt; }

    public Instant getBeaconDeactivatedAt() { return beaconDeactivatedAt; }
    public void setBeaconDeactivatedAt(Instant beaconDeactivatedAt) { this.beaconDeactivatedAt = beaconDeactivatedAt; }

    public List<PresentStudent> getPresentStudents() { return presentStudents; }
    public void setPresentStudents(List<PresentStudent> presentStudents) { this.presentStudents = presentStudents; }


    public void setRoomNumber(String roomNumber) {
        this.RoomNumber = roomNumber;
    }
}
