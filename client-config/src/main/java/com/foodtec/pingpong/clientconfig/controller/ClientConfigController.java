package com.foodtec.pingpong.clientconfig.controller;

import com.foodtec.pingpong.clientconfig.exception.ClientIdNotFoundException;
import com.foodtec.pingpong.clientconfig.model.ClientConfig;
import com.foodtec.pingpong.clientconfig.service.ClientConfigMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client-config")
@RequiredArgsConstructor
public class ClientConfigController {
    private final ClientConfigMapping clientConfigMapping;

    @GetMapping("/{client-id}")
    public ClientConfig getConfig(@PathVariable("client-id") String clientId) throws ClientIdNotFoundException {
        return clientConfigMapping.getConfigById(clientId);
    }

    @RequestMapping("/echo")
    public String echo() {
        return "Echo from client-config";
    }
}
