package com.foodtec.pingpong.clientconfig.service;

import com.foodtec.pingpong.clientconfig.exception.ClientIdNotFoundException;
import com.foodtec.pingpong.clientconfig.model.ClientConfig;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class HardcodedClientConfigMapping implements ClientConfigMapping {
    private final Map<String, ClientConfig> clientConfigMap;

    public HardcodedClientConfigMapping() {
        this.clientConfigMap = Stream.of(
                new ClientConfig("123", true, false, true, "abcd"),
                new ClientConfig("122", true, true, true, "abbc"),
                new ClientConfig("121", true, true, false, "aabb")
        ).collect(Collectors.toMap(ClientConfig::clientId, Function.identity()));
    }

    @Override
    public ClientConfig getConfigById(String clientId) throws ClientIdNotFoundException {
        return Optional.ofNullable(clientConfigMap.get(clientId))
                .orElseThrow(()-> new ClientIdNotFoundException("Client id not found: " + clientId));
    }
}
