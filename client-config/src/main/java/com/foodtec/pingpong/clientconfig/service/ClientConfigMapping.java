package com.foodtec.pingpong.clientconfig.service;

import com.foodtec.pingpong.clientconfig.exception.ClientIdNotFoundException;
import com.foodtec.pingpong.clientconfig.model.ClientConfig;

import java.util.List;

public interface ClientConfigMapping {
    ClientConfig getConfigById(String clientId) throws ClientIdNotFoundException;
}
