package com.foodtec.pingpong.clientconfig.controller;


import com.foodtec.pingpong.clientconfig.exception.ClientIdNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
@Slf4j
public class ClientConfigControllerAdvice {
    @ExceptionHandler(ClientIdNotFoundException.class)
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
