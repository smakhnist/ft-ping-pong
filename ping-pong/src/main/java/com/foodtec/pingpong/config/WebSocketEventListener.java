package com.foodtec.pingpong.config;

import com.foodtec.pingpong.service.ClientConnectionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpAttributes;
import org.springframework.messaging.simp.SimpAttributesContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import static com.foodtec.pingpong.config.PingPongAppConstants.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketEventListener {
    private final ClientConnectionManager clientConnectionManager;

    @EventListener
    private void handleSessionConnected(SessionConnectEvent event) {
        SimpAttributes simpAttributes = SimpAttributesContextHolder.currentAttributes();
        String clientId = (String) simpAttributes.getAttribute(CLIENT_ID_WS_HEADER_PARAM);
        String sessionId = simpAttributes.getSessionId();
        clientConnectionManager.addSession(clientId, sessionId);
        log.info("client-id -> session established: {} -> {}", clientId, sessionId);
    }



    @EventListener
    private void handleSessionDisconnect(SessionDisconnectEvent event) {
        String sessionId = SimpAttributesContextHolder.currentAttributes().getSessionId();
        clientConnectionManager.removeSession(sessionId);
        log.info("Session {} has been disconnected", sessionId);
    }
}
