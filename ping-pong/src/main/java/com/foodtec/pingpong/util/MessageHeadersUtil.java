package com.foodtec.pingpong.util;

import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;

public class MessageHeadersUtil {
    public static MessageHeaders buildHeaders(SimpMessageType messageType, String sessionId) {
        SimpMessageHeaderAccessor messageHeaderAccessor = SimpMessageHeaderAccessor.create(messageType);
        messageHeaderAccessor.setSessionId(sessionId);
        return messageHeaderAccessor.getMessageHeaders();
    }
}