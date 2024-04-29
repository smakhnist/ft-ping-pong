package com.foodtec.pingpong.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
@Slf4j
public class PingPongControllerAdvice {

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<String> handleException(
            RuntimeException ex, WebRequest request) {
        log.error("An INTERNAL_SERVER_ERROR error occurred", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }
}
