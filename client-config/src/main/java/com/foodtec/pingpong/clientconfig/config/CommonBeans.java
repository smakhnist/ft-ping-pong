package com.foodtec.pingpong.clientconfig.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class CommonBeans {
    @Bean
    public RestTemplate RestTemplate() {
        return new RestTemplateBuilder().build();
    }
}
