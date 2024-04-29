package com.foodtec.pingpong.config;

public interface PingPongAppConstants {
    String WS_ENDPOINT = "/socket/ping-pong";
    String WS_DESTINATION_PREFIX = "/acceptance";
    String WS_TOPIC_DESTINATION = WS_DESTINATION_PREFIX + "/ping-pong"; // 'ping-pong' topic name

    String AUTH_ID_WS_HEADER_PARAM = "Auth-id";
    String CLIENT_ID_WS_HEADER_PARAM = "Client-id";
}
