package com.example.smart_attendance.config;


import com.example.smart_attendance.dto.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MessageResponse badRequest(IllegalArgumentException ex){
        return new MessageResponse(ex.getMessage());
    }
}
