package com.foodtec.pingpong.clientconfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ClientConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClientConfigApplication.class, args);
    }

}
