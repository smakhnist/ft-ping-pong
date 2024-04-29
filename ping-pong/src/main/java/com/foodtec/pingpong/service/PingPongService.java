package com.foodtec.pingpong.service;

import org.springframework.http.ResponseEntity;

public interface PingPongService {
    ResponseEntity<String> ping(String clientId);

    ResponseEntity<String> pong(String clientId);

    ResponseEntity<String> drop(String clientId);
}
