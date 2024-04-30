package com.foodtec.pingpong.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.converter.CompositeMessageConverter;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.List;
import java.util.Scanner;

@Slf4j
public class ClientAppMain {
    private static final String WS_TOPIC = "/acceptance/ping-pong";
    private static final String WS_ENDPOINT = "ws://localhost:8080/socket/ping-pong";
    private static final String CLIENT_ID_WS_HEADER_PARAM = "Client-id";
    private static final String AUTH_ID_WS_HEADER_PARAM = "Auth-id";

    public static void main(String[] args) {
        log.info("Starting WebSocket client...");
        final String DEFAULT_CLIENT_ID = "123";
        final String DEFAULT_AUTH_ID = "abcd";

        if (args.length == 2) {
            connect(args[0], args[1]);
        } else if (args.length == 0) {
            connect(DEFAULT_CLIENT_ID, DEFAULT_AUTH_ID);
        } else {
            log.error("Invalid number of arguments. Expected 0 or 2 arguments. Exiting...");
            System.exit(1);
        }

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if ("exit".equals(line)) {
                break;
            }
        }
    }

    private static void connect(String clientId, String authId) {
        WebSocketStompClient stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        stompClient.setMessageConverter(new CompositeMessageConverter(List.of(new MappingJackson2MessageConverter(), new StringMessageConverter())));

        WebSocketHttpHeaders handshakeHeaders = new WebSocketHttpHeaders();
        handshakeHeaders.add(CLIENT_ID_WS_HEADER_PARAM, clientId);
        handshakeHeaders.add(AUTH_ID_WS_HEADER_PARAM, authId);


        stompClient.connectAsync(WS_ENDPOINT, handshakeHeaders, new StompHeaders(), new ClientStompSessionHandler(() -> connect(clientId, authId)));
    }

    private static class ClientStompSessionHandler extends StompSessionHandlerAdapter {
        private final Runnable reconnectCallback;

        public ClientStompSessionHandler(Runnable reconnectCallback) {
            this.reconnectCallback = reconnectCallback;
        }

        @Override
        public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
            log.error("Got an exception in session {}:", session.getSessionId(), exception);
        }

        @Override
        public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
            log.info("New session established: {}. Connecting to topic: {}", session.getSessionId(), WS_TOPIC);
            session.subscribe(WS_TOPIC, this);
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            log.info("Received: " + payload);
        }

        @Override
        public void handleTransportError(StompSession session, Throwable exception) {
            log.error("Got an exception: '{}' for session {}. Reconnecting the client...", exception.getMessage(), session.getSessionId());
            try {
                reconnectCallback.run();
            } catch (Exception e) {
                log.error("Failed to reconnect. Error: {}", e.getMessage());
            }
        }
    }
}