package com.foodtec.pingpong.websocket;

import com.foodtec.pingpong.model.ClientConfig;
import com.foodtec.pingpong.security.DDoSAttackerDetector;
import com.foodtec.pingpong.service.ClientConfigObtainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;
import java.util.Optional;

import static com.foodtec.pingpong.config.PingPongAppConstants.AUTH_ID_WS_HEADER_PARAM;
import static com.foodtec.pingpong.config.PingPongAppConstants.CLIENT_ID_WS_HEADER_PARAM;

@Slf4j
@RequiredArgsConstructor
@Component
public class HttpSessionIdHandshakeInterceptor implements HandshakeInterceptor {
    private final ClientConfigObtainService clientConfigObtainService;
    private final DDoSAttackerDetector ddosAttackerDetector;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        log.info("beforeHandshake");

        String clientId = getHeaderValue(request, CLIENT_ID_WS_HEADER_PARAM);
        String authId = getHeaderValue(request, AUTH_ID_WS_HEADER_PARAM);

        if (clientId == null || authId == null) {
            log.warn("attempt of unauthorized connection");
            return false;
        }

        if (!ddosAttackerDetector.check(clientId)) { // to prevent DDoS attacks
            log.warn("Probable DDoS attack detected for clientId: {}", clientId);
            return false;
        }

        try {
            ClientConfig clientConfig = clientConfigObtainService.getClientConfig(clientId);
            if (!authId.equals(clientConfig.authId())) {
                log.warn("Authorization failed for clientId: {}", clientId);
                return false;
            }

            if (clientConfig.canConnect()) {
                attributes.put(CLIENT_ID_WS_HEADER_PARAM, clientId);
                return true;
            } else {
                log.warn("Client is not allowed to connect: {}", clientId);
                return false;
            }
        } catch (Exception e) {
            log.error("Failed to get client config for clientId: {}", clientId, e);
            return false;
        }
    }

    private static String getHeaderValue(ServerHttpRequest request, String headerName) {
        return Optional.ofNullable(request.getHeaders().get(headerName))
                .map(l -> !l.isEmpty() ? l.get(0) : null)
                .orElse(null);
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
        log.info("afterHandshake");
    }
}
