package com.foodtec.pingpong.websocket;

import com.foodtec.pingpong.service.ClientConnectionManager;
import com.foodtec.pingpong.util.MessageHeadersUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.messaging.simp.SimpAttributes;
import org.springframework.messaging.simp.SimpAttributesContextHolder;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.foodtec.pingpong.config.PingPongAppConstants.CLIENT_ID_WS_HEADER_PARAM;
import static com.foodtec.pingpong.config.PingPongAppConstants.WS_TOPIC_DESTINATION;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketEventListener {
    private final ClientConnectionManager clientConnectionManager;
    private final MessageSendingOperations<String> messageSendingOperations;

    @EventListener
    private void handleSessionConnected(SessionConnectEvent event) {
        SimpAttributes simpAttributes = SimpAttributesContextHolder.currentAttributes();
        String clientId = (String) simpAttributes.getAttribute(CLIENT_ID_WS_HEADER_PARAM);
        String sessionId = simpAttributes.getSessionId();
        clientConnectionManager.addSession(clientId, sessionId);
        log.info("client-id -> session established: {} -> {}", clientId, sessionId);
    }

    @EventListener
    private void handleSessionSubscribeEvent(SessionSubscribeEvent sessionSubscribeEvent) {
        SimpAttributes simpAttributes = SimpAttributesContextHolder.currentAttributes();

        String sessionId = simpAttributes.getSessionId();
        String clientId = (String) simpAttributes.getAttribute(CLIENT_ID_WS_HEADER_PARAM);
        String topic = SimpMessageHeaderAccessor.wrap(sessionSubscribeEvent.getMessage()).getDestination();
        log.info("Client-id {} has been subscribed to the topic {}", clientId, topic);

        // Send subscription acknowledgement to the client
        Executors.newSingleThreadScheduledExecutor().schedule(
                () -> messageSendingOperations.convertAndSend(WS_TOPIC_DESTINATION,
                        String.format("server '%s' topic subscription acknowledgement", topic),
                        MessageHeadersUtil.buildHeaders(SimpMessageType.MESSAGE, sessionId)),
                1000, TimeUnit.MILLISECONDS);
    }

    @EventListener
    private void handleSessionDisconnect(SessionDisconnectEvent event) {
        String sessionId = SimpAttributesContextHolder.currentAttributes().getSessionId();
        clientConnectionManager.removeSession(sessionId);
        log.info("Session {} has been disconnected", sessionId);
    }
}
