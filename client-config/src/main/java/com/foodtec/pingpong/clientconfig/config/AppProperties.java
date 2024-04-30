package com.foodtec.pingpong.clientconfig.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "client-config")
@Getter
@Setter
public class AppProperties {
    private String securityAuthorizationEndpoint;
}
