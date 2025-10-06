package com.example.smart_attendance.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Document(collection = "classrooms")
public class Classroom {
    @Id
    private String id;

    private String department;
    private String className;   // e.g., "TE-CSE-A"
    private String subject;
    private String beaconUuid;  // uuid string of beacon in that room
    private String roomNumber;  // <-- NEW FIELD

    public Classroom() {}

    // getters & setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getBeaconUuid() { return beaconUuid; }
    public void setBeaconUuid(String beaconUuid) { this.beaconUuid = beaconUuid; }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

    public void setSchedule(String schedule) {

    }
}
