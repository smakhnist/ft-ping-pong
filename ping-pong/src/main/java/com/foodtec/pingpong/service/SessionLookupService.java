package com.foodtec.pingpong.service;

import java.util.Optional;

public interface SessionLookupService {
    Optional<String> getSession(String clientId);
}
