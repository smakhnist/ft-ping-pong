package com.foodtec.pingpong.service.impl;

import com.foodtec.pingpong.config.PingPongAppConstants;
import com.foodtec.pingpong.service.ClientConfigObtainService;
import com.foodtec.pingpong.service.SessionLookupService;
import com.foodtec.pingpong.service.PingPongService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
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
                messageSendingOperations.convertAndSend(PingPongAppConstants.WS_TOPIC_DESTINATION, "ping", buildMessageHeaders(SimpMessageType.MESSAGE, sessionIdOptional.get()));
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
                messageSendingOperations.convertAndSend(PingPongAppConstants.WS_TOPIC_DESTINATION, "pong", buildMessageHeaders(SimpMessageType.MESSAGE, sessionIdOptional.get()));
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
            messageSendingOperations.convertAndSend(PingPongAppConstants.WS_TOPIC_DESTINATION, "disconnect", buildMessageHeaders(SimpMessageType.DISCONNECT, sessionIdOptional.get()));
            return logAndRespond(HttpStatus.OK, "Client-id %s has been disconnected", clientId);
        }
    }

    private MessageHeaders buildMessageHeaders(SimpMessageType messageType, String sessionId) {
        SimpMessageHeaderAccessor messageHeaderAccessor = SimpMessageHeaderAccessor.create(messageType);
        messageHeaderAccessor.setSessionId(sessionId);
        return messageHeaderAccessor.getMessageHeaders();
    }

    private static ResponseEntity<String> logAndRespond(HttpStatus status, String template, Object... args) {
        String message = String.format(template, args);
        log.info(message);
        return ResponseEntity.status(status).body(message);
    }
}
