package com.foodtec.pingpong.service;

import com.foodtec.pingpong.model.ClientConfig;

public interface ClientConfigObtainService {
    ClientConfig getClientConfig(String clientId);
}
