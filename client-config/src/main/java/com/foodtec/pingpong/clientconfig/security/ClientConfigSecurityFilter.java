package com.foodtec.pingpong.clientconfig.security;

import com.foodtec.pingpong.clientconfig.config.AppProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Objects;


public class ClientConfigSecurityFilter extends GenericFilterBean {
    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        String authorizationHeader = httpRequest.getHeader("Authorization");
        if (authorizationHeader == null
                || !authorizationHeader.startsWith("Basic ")) {
            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorization header is missing or invalid.");
        }

        String token = Objects.requireNonNull(authorizationHeader).substring("Basic ".length());

        ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
        RestTemplate restTemplate = ctx.getBean(RestTemplate.class);
        AppProperties appProperties = ctx.getBean(AppProperties.class);

        var respEntity = restTemplate.getForEntity(appProperties.getSecurityAuthorizationEndpoint(), Void.class, token);
        if (!respEntity.getStatusCode().is2xxSuccessful()) {
            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "The token is not valid.");
        }

        chain.doFilter(request, response);
    }
}

