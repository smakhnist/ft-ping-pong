package com.foodtec.pingpong.clientconfig.model;

public record ClientConfig(
        String clientId,
        boolean canPing,
        boolean canPong,
        boolean canConnect,
        String authId) {
}
