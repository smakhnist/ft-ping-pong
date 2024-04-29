package com.foodtech.pingpong.securityservice.controller;

import com.foodtech.pingpong.securityservice.exception.UndefinedApplicationException;
import com.foodtech.pingpong.securityservice.service.TokenMappingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/security")
@Slf4j
public class SecurityController {
    private final TokenMappingService tokenMappingService;

    public SecurityController(TokenMappingService tokenMappingService) {
        this.tokenMappingService = tokenMappingService;
    }

    @GetMapping("/{application-id}/token")
    public String getToken(@PathVariable("application-id") String applicationId) throws UndefinedApplicationException {
        log.info("Getting token for application {}", applicationId);
        return tokenMappingService.getToken(applicationId);
    }

    @GetMapping("/{application-id}/valid/{token}")
    public void authorize(@PathVariable("application-id") String applicationId, @PathVariable("token") String token) throws IllegalAccessException {
        log.info("Authorizing application {}", applicationId);
        tokenMappingService.authorize(applicationId, token);
    }

    @GetMapping("/echo")
    public String echo() {
        return "Echo from security-service";
    }

}
