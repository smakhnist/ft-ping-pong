package com.foodtech.pingpong.securityservice.controller;

import com.foodtech.pingpong.securityservice.exception.UndefinedApplicationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class SecurityServiceControllerAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler(IllegalAccessException.class)
    protected ResponseEntity<String> handleUnauthorized(
            Exception ex, WebRequest request) {
        log.warn("Unauthorized access");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    @ExceptionHandler(UndefinedApplicationException.class)
    protected ResponseEntity<String> handleApplicationNotFound(
            Exception ex, WebRequest request) {
        log.warn(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<String> handleException(
            RuntimeException ex, WebRequest request) {
        log.error("An INTERNAL_SERVER_ERROR error occurred", ex);
        return ResponseEntity.status(500).body(ex.getMessage());
    }
}
