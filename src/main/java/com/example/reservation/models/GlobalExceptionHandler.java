package com.example.reservation.models;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ReservationAPIException.class)
    public ResponseEntity<ErrorDetails> handleReservationAPIException(
            ReservationAPIException exception,
            WebRequest request
    ) {
        final ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setErrorCode(exception.getStatus().value());
        errorDetails.setErrorMessage(exception.getLocalizedMessage());
        errorDetails.setDevErrorMessage(request.getDescription(false));
        errorDetails.setTimeOfError(System.currentTimeMillis());

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDetails> handleAccessDeniedException(
            AccessDeniedException exception,
            WebRequest request
    ) {
        final ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setErrorMessage(exception.getLocalizedMessage());
        errorDetails.setDevErrorMessage(request.getDescription(false));
        errorDetails.setTimeOfError(System.currentTimeMillis());

        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGlobalException(
            Exception exception,
            WebRequest request
    ) {
        final ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setErrorMessage(exception.getLocalizedMessage());
        errorDetails.setDevErrorMessage(request.getDescription(false));
        errorDetails.setTimeOfError(System.currentTimeMillis());

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
