package com.foodtec.pingpong.clientconfig.config;


import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "client-config")
@Data
public class AppProperties {
    private String securityAuthorizationEndpoint;
}
