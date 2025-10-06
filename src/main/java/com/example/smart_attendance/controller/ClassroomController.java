package com.example.smart_attendance.controller;

import com.example.smart_attendance.dto.MessageResponse;
import com.example.smart_attendance.dto.requests.RegisterClassroomRequest;
import com.example.smart_attendance.model.Classroom;
import com.example.smart_attendance.service.ClassroomService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/classrooms")
public class ClassroomController {
    private final ClassroomService classroomService;

    public ClassroomController(ClassroomService classroomService) {
        this.classroomService = classroomService;
    }

    @GetMapping
    public List<Classroom> getAll() {
        return classroomService.getAllClassrooms();
    }

    @PostMapping
    public MessageResponse create(@Valid @RequestBody RegisterClassroomRequest req) {
        Classroom classroom = classroomService.createClassroom(
                req.className(),
                req.subject(),
                req.roomNumber(),
                req.beaconUuid()
        );
        return new MessageResponse("Classroom created with Beacon UUID: " + classroom.getBeaconUuid());
    }
}
