package com.foodtec.pingpong.service.impl;

import com.foodtec.pingpong.service.ClientConnectionManager;
import com.foodtec.pingpong.service.SessionLookupService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory implementation of {@link ClientConnectionManager} and {@link SessionLookupService}.
 * In real world, this should be replaced with distributed cache (Redis)
 */

@Service
public class InMemoryClientConnectionManager implements ClientConnectionManager, SessionLookupService {
    private final Map<String, String> userIdToSessionId = new ConcurrentHashMap<>();


    public void addSession(String userId, String sessionId) {
        userIdToSessionId.put(userId, sessionId);
    }

    public void removeSession(String sessionId) {
        userIdToSessionId.entrySet().removeIf(entry -> entry.getValue().equals(sessionId));
    }

    public Optional<String> getSession(String userId) {
        return Optional.ofNullable(userIdToSessionId.get(userId));
    }
}
