package com.foodtec.pingpong.service;

public interface ClientConnectionManager {
    void addSession(String userId, String sessionId);
    void removeSession(String sessionId);
}
