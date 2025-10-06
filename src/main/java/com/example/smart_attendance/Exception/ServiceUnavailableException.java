package com.example.smart_attendance.Exception;
// Should be 'exception' not 'Exception'

public class ServiceUnavailableException extends RuntimeException {
    public ServiceUnavailableException(String message) {
        super(message);
    }
}