package com.foodtec.pingpong.service.impl;

import com.foodtec.pingpong.service.ClientSessionManager;
import com.foodtec.pingpong.service.SessionLookupService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory implementation of {@link ClientSessionManager} and {@link SessionLookupService}.
 * In real world, this should be replaced with distributed cache (Redis)
 */

@Service
public class InMemoryClientSessionManager implements ClientSessionManager, SessionLookupService {
    private final Map<String, String> clientIdToSessionId = new ConcurrentHashMap<>();

    public void addSession(String clientId, String sessionId) {
        clientIdToSessionId.put(clientId, sessionId);
    }

    public void removeSession(String sessionId) {
        clientIdToSessionId.entrySet().removeIf(entry -> entry.getValue().equals(sessionId));
    }

    public Optional<String> getSession(String clientId) {
        return Optional.ofNullable(clientIdToSessionId.get(clientId));
    }
}
