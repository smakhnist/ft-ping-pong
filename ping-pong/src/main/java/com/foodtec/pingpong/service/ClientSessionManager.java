package com.foodtec.pingpong.service;

public interface ClientSessionManager {
    void addSession(String clientId, String sessionId);

    void removeSession(String sessionId);
}
