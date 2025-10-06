package com.example.smart_attendance.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "teachers")
public class Teacher {
    @Id
    private String id;

    private String name;
    private String department;

    @Indexed(unique = true)
    private String email;

    private String passwordHash; // store BCrypt hash

    @Indexed(unique = true)
    private String teacherId;

    private String profilePicUrl; // or store a file key
    private Instant createdAt = Instant.now();

    public Teacher() {}

    public Teacher(String name, String department, String email, String passwordHash, String teacherId, String profilePicUrl) {
        this.name = name;
        this.department = department;
        this.email = email;
        this.passwordHash = passwordHash;
        this.teacherId = teacherId;
        this.profilePicUrl = profilePicUrl;
    }

    // getters & setters

    public String getId() { return id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getDepartment() { return department; }

    public void setDepartment(String department) { this.department = department; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }

    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getTeacherId() { return teacherId; }

    public void setTeacherId(String teacherId) { this.teacherId = teacherId; }

    public String getProfilePicUrl() { return profilePicUrl; }

    public void setProfilePicUrl(String profilePicUrl) { this.profilePicUrl = profilePicUrl; }

    public Instant getCreatedAt() { return createdAt; }

    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
