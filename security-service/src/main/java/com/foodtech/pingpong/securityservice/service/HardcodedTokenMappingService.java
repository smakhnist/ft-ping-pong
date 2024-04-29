package com.foodtech.pingpong.securityservice.service;

import com.foodtech.pingpong.securityservice.exception.UndefinedApplicationException;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class HardcodedTokenMappingService implements TokenMappingService {
    private final Map<String, String> tokenMap = Map.of(
            "ping-pong", "pp12345678", // 10 chars
            "some-app", "sa12345678"
    );

    public String getToken(String appName) throws UndefinedApplicationException {
        return Optional.ofNullable(tokenMap.get(appName)).orElseThrow(() -> new UndefinedApplicationException("Application not found: " + appName));
    }

    public void authorize(String appName, String token) throws IllegalAccessException {
        if (!token.equals(tokenMap.get(appName))) {
            throw new IllegalAccessException("Invalid token");
        }
    }
}
