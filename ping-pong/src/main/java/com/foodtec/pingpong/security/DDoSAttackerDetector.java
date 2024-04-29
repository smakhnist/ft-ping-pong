package com.foodtec.pingpong.security;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This service is used to prevent DDoS attacks.
 * It allows only 10 connection attempts per minute from a single client.
 * If the client exceeds this limit, the service will return false.
 */
@Component
public class DDoSAttackerDetector {
    private static final int MAX_REQUESTS_PER_MINUTE = 10;
    private final Map<String, List<LocalDateTime>> clientRequests = new ConcurrentHashMap<>();

    public boolean check(String clientId) {
        clientRequests.putIfAbsent(clientId, new LinkedList<>());
        List<LocalDateTime> localDateTimes = clientRequests.get(clientId);
        synchronized (localDateTimes) {
            if (localDateTimes.size() >= MAX_REQUESTS_PER_MINUTE) {
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime first = localDateTimes.get(0);
                if (first.isAfter(now.minusMinutes(1))) {
                    return false;
                } else {
                    localDateTimes.add(now); // O(1)
                    localDateTimes.remove(0); // O(1)
                }
            } else {
                localDateTimes.add(LocalDateTime.now());
            }
        }
        return true;
    }
}
