package com.foodtec.pingpong.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import static com.foodtec.pingpong.config.PingPongAppConstants.WS_DESTINATION_PREFIX;
import static com.foodtec.pingpong.config.PingPongAppConstants.WS_ENDPOINT;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Autowired
    private WebSocketHandshakeInterceptor webSocketHandshakeInterceptor;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker(WS_DESTINATION_PREFIX);
//        config.setApplicationDestinationPrefixes("/app"); // if we need to accept commands from ws clients
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(WS_ENDPOINT)
                .addInterceptors(webSocketHandshakeInterceptor);
    }
}