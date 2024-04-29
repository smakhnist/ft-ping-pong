package com.foodtech.pingpong.securityservice.service;

import com.foodtech.pingpong.securityservice.exception.UndefinedApplicationException;

public interface TokenMappingService {
    String getToken(String appName) throws UndefinedApplicationException;

    void authorize(String appName, String token) throws IllegalAccessException;
}
