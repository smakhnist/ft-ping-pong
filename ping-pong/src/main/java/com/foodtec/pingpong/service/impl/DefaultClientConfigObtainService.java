package com.foodtec.pingpong.service.impl;

import com.foodtec.pingpong.exception.ClientConfigServiceException;
import com.foodtec.pingpong.exception.SecurityServiceException;
import com.foodtec.pingpong.service.ClientConfigObtainService;
import com.foodtec.pingpong.util.ResponseEntityUtil;
import com.foodtec.pingpong.config.AppProperties;
import com.foodtec.pingpong.model.ClientConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DefaultClientConfigObtainService implements ClientConfigObtainService {
    private final RestTemplate restTemplate;
    private final AppProperties appProperties;

    @Override
    public ClientConfig getClientConfig(String clientId) {
        ResponseEntity<String> clientConfigAppTokenRE = restTemplate.getForEntity(appProperties.getSecurityTokenObtainEndpoint(), String.class);
        String clientConfigAppTokenId = ResponseEntityUtil.getBodyOrThrow(clientConfigAppTokenRE,
                () -> new SecurityServiceException("Failed to get client config app token"));

        MultiValueMap<String, String> headers = new MultiValueMapAdapter<>(Map.of("Authorization", List.of("Basic " + clientConfigAppTokenId)));
        ResponseEntity<ClientConfig> clientConfigRE = restTemplate.exchange(appProperties.getClientConfigEndpoint(), HttpMethod.GET, new HttpEntity<>(null, headers), ClientConfig.class, clientId);
        return ResponseEntityUtil.getBodyOrThrow(clientConfigRE, () -> new ClientConfigServiceException("Failed to get client config for client id: " + clientId));
    }
}
