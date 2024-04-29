package com.foodtec.pingpong.controller;

import com.foodtec.pingpong.service.PingPongService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PingPongController {
    private final PingPongService pingPongService;

    @RequestMapping("/echo")
    public String echo() {
        return "Echo from ping-pong";
    }

    @PostMapping("/acceptance/{client-id}/ping")
    public ResponseEntity<String> ping(@PathVariable("client-id") String clientId) {
        log.info("Ping called for client-id: {}", clientId);
        return pingPongService.ping(clientId);
    }

    @PostMapping("/acceptance/{client-id}/pong")
    public ResponseEntity<String> pong(@PathVariable("client-id") String clientId) {
        log.info("Pong called for client-id: {}", clientId);
        return pingPongService.pong(clientId);
    }

    @PutMapping("/connection/{client-id}/drop")
    public ResponseEntity<String> drop(@PathVariable("client-id") String clientId) {
        log.info("Drop called for client-id: {}", clientId);
        return pingPongService.drop(clientId);
    }
}
