package com.foodtec.pingpong.model;

public record ClientConfig(
        String clientId,
        boolean canPing,
        boolean canPong,
        boolean canConnect,
        String authId) {
}
