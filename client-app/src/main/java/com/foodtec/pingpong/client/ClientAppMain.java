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
    public static final String WS_ENDPOINT = "/socket/ping-pong";


    public static void main(String[] args) {
        System.out.println("Hello world!");

        connect();

        new Scanner(System.in).nextLine(); // Don't close immediately.
    }

    private static void connect() {
        WebSocketStompClient stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        stompClient.setMessageConverter(new CompositeMessageConverter(List.of(new MappingJackson2MessageConverter(), new StringMessageConverter())));

        WebSocketHttpHeaders handshakeHeaders = new WebSocketHttpHeaders();
        handshakeHeaders.add("Auth-id", "abcd");
        handshakeHeaders.add("Client-id", "123");


        StompHeaders stompHeaders = new StompHeaders();
        stompHeaders.add("Auth-id", "abcd");
        stompHeaders.add("Client-id", "123");

        stompClient.connectAsync("ws://localhost:8080" + WS_ENDPOINT, handshakeHeaders, stompHeaders, new MyStompSessionHandler());
    }

    private static class MyStompSessionHandler extends StompSessionHandlerAdapter {
        @Override
        public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
            log.error("Got an exception in session {}", session.getSessionId(), exception);
        }

        @Override
        public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
            session.subscribe(WS_TOPIC, this);
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            log.info("Received: " + payload);
        }

        @Override
        public void handleTransportError(StompSession session, Throwable exception) {
            log.error("Got an exception for session {}", session.getSessionId(), exception);
            try {
                connect();
            } catch (Exception e) {
                log.error("Failed to reconnect", e);
            }
        }
    }
}