
package com.example.smart_attendance.service;

import com.example.smart_attendance.model.Classroom;
import com.example.smart_attendance.repository.ClassroomRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
// ... other imports

    @Service
    public class ClassroomService {

        // A constant namespace to make the generated UUIDs unique to your app
        private static final UUID NAMESPACE_UUID = UUID.fromString("a3b8f47a-6b4c-4a3a-9383-2a89a74c6de2");
        private final ClassroomRepository repo;

        public List<Classroom> getAllClassrooms() {
            return repo.findAll();
        }

        public ClassroomService(ClassroomRepository repo) { this.repo = repo; }

             public Classroom createClassroom(String className, String subject, String roomNumber, String beaconUuid) {
            Classroom classroom = new Classroom();
            classroom.setClassName(className);
            classroom.setSubject(subject);
            classroom.setRoomNumber(roomNumber);
            classroom.setBeaconUuid(beaconUuid); // Set the UUID from the parameter

            // return classroomRepository.save(classroom); // Save the new classroom
            return classroom; // Placeholder return
        }
    }// ... other methods like getAllClassrooms() ...


