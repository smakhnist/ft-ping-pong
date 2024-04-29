package com.foodtec.pingpong.clientconfig.service;

import com.foodtec.pingpong.clientconfig.exception.ClientIdNotFoundException;
import com.foodtec.pingpong.clientconfig.model.ClientConfig;

public interface ClientConfigMapping {
    ClientConfig getConfigById(String clientId) throws ClientIdNotFoundException;
}
