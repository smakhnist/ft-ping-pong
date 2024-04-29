package com.foodtec.pingpong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class PingPongApplication {

    public static void main(String[] args) {
        SpringApplication.run(PingPongApplication.class, args);
    }

}
