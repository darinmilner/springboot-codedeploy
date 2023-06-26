package com.example.reservation.models;

import org.springframework.http.HttpStatus;

public class ReservationAPIException extends RuntimeException {
    private final HttpStatus status;
    private final String message;

    public ReservationAPIException(String message, HttpStatus status) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
