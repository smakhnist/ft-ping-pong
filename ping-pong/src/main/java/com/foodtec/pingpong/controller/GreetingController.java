package com.foodtec.pingpong.controller;

import com.foodtec.pingpong.config.PingPongAppConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
@Slf4j
@ConditionalOnExpression("false") // just remove @ConditionalOnExpression to enable 2-way communication with clients
public class GreetingController {
    @MessageMapping("/hello")
    @SendTo(PingPongAppConstants.WS_TOPIC_DESTINATION)
    public String greeting(String message, @Headers Map<String, String> headers) throws Exception {
        log.info("Received message: {}", message);
        return "Hello, " + message + "!";
    }
}
