package com.mytest.geteway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Component
@Slf4j
public class RequestUriAuthGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {
    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            if (exchange.getRequest().getURI().getPath().contains("/feign/")){
                return buildResp(exchange);
            }

            if (exchange.getRequest().getURI().getPath().contains("/internal/")
                    && !exchange.getRequest().getURI().getHost().contains(".intra.")){
                return buildResp(exchange);
            }
            return chain.filter(exchange);
        };
    }

    private Mono<Void> buildResp(ServerWebExchange exchange){
        ServerHttpResponse response = exchange.getResponse();

        MediaType mediaType = exchange.getRequest().getHeaders().getContentType();

        HttpHeaders httpHeaders = response.getHeaders();
        httpHeaders.add("Content-Type", mediaType!=null?mediaType.toString():MediaType.APPLICATION_JSON_UTF8_VALUE);
        httpHeaders.add("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");

        DataBuffer bodyDataBuffer = response.bufferFactory()
                .wrap("{\"statusCode\":401,\"message\":\"Request Uri Not Allowed\"}"
                        .getBytes());
        return response.writeWith(Mono.just(bodyDataBuffer));
    }
}