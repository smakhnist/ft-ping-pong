package com.foodtec.pingpong.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ping-pong")
@Getter
@Setter
public class AppProperties {
    private String securityTokenObtainEndpoint;
    private String clientConfigEndpoint;
}
