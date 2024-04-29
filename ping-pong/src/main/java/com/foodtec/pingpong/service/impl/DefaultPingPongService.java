package com.foodtec.pingpong.service.impl;

import com.foodtec.pingpong.config.PingPongAppConstants;
import com.foodtec.pingpong.service.ClientConfigObtainService;
import com.foodtec.pingpong.service.PingPongService;
import com.foodtec.pingpong.service.SessionLookupService;
import com.foodtec.pingpong.util.MessageHeadersUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultPingPongService implements PingPongService {
    private final MessageSendingOperations<String> messageSendingOperations;
    private final SessionLookupService sessionLookupService;
    private final ClientConfigObtainService clientConfigObtainService;

    public ResponseEntity<String> ping(String clientId) {
        Optional<String> sessionIdOptional = sessionLookupService.getSession(clientId);
        if (sessionIdOptional.isEmpty()) {
            return logAndRespond(HttpStatus.NOT_FOUND, "No sessionIdOptional found for client-id: %s", clientId);
        } else {
            if (clientConfigObtainService.getClientConfig(clientId).canPing()) {
                messageSendingOperations.convertAndSend(PingPongAppConstants.WS_TOPIC_DESTINATION, "ping", MessageHeadersUtil.buildHeaders(SimpMessageType.MESSAGE, sessionIdOptional.get()));
                return logAndRespond(HttpStatus.OK, "Ping sent to client-id: %s", clientId);
            } else {
                return logAndRespond(HttpStatus.FORBIDDEN, "Client-id: %s is not granted to ping", clientId);
            }
        }
    }

    public ResponseEntity<String> pong(String clientId) {
        Optional<String> sessionIdOptional = sessionLookupService.getSession(clientId);
        if (sessionIdOptional.isEmpty()) {
            return logAndRespond(HttpStatus.NOT_FOUND, "No sessionIdOptional found for client-id: %s", clientId);
        } else {
            if (clientConfigObtainService.getClientConfig(clientId).canPong()) {
                messageSendingOperations.convertAndSend(PingPongAppConstants.WS_TOPIC_DESTINATION, "pong", MessageHeadersUtil.buildHeaders(SimpMessageType.MESSAGE, sessionIdOptional.get()));
                return logAndRespond(HttpStatus.OK, "Pong sent to client-id: %s", clientId);
            } else {
                return logAndRespond(HttpStatus.FORBIDDEN, "Client-id: %s is not granted to pong", clientId);
            }
        }
    }

    public ResponseEntity<String> drop(String clientId) {
        Optional<String> sessionIdOptional = sessionLookupService.getSession(clientId);
        if (sessionIdOptional.isEmpty()) {
            return logAndRespond(HttpStatus.NOT_FOUND, "No sessionIdOptional found for client-id: %s", clientId);
        } else {
            messageSendingOperations.convertAndSend(PingPongAppConstants.WS_TOPIC_DESTINATION, "disconnect", MessageHeadersUtil.buildHeaders(SimpMessageType.DISCONNECT, sessionIdOptional.get()));
            return logAndRespond(HttpStatus.OK, "Client-id %s has been disconnected", clientId);
        }
    }

    private static ResponseEntity<String> logAndRespond(HttpStatus status, String template, Object... args) {
        String message = String.format(template, args);
        log.info(message);
        return ResponseEntity.status(status).body(message);
    }
}
