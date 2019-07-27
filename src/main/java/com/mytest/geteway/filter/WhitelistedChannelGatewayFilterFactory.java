package com.mytest.geteway.filter;

import com.mytest.geteway.utils.NetworkHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

//@Component
@Slf4j
public class WhitelistedChannelGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {
    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            for (Map.Entry<String, List<String>> entry : exchange.getRequest().getQueryParams().entrySet()) {
                log.info("--->{}:{}",entry.getKey(),entry.getValue().get(0));
            }

            String ipAddr = NetworkHelper.getRemoteIpAddr(exchange.getRequest());

            log.info("ipAddr----->{}",ipAddr);

            if (Whitelist.contains(ipAddr)){
                return chain.filter(exchange);
            }

            String uri = exchange.getRequest().getURI().toString();

            ServerHttpResponse response = exchange.getResponse();

            HttpHeaders httpHeaders = response.getHeaders();
            httpHeaders.add("Content-Type", "application/json; charset=UTF-8");
            httpHeaders.add("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");

            DataBuffer bodyDataBuffer = response.bufferFactory()
                    .wrap("{\"statusCode\":400,\"message\":\"IP Not Allowed\"}"
                            .getBytes());
            return response.writeWith(Mono.just(bodyDataBuffer));
        };
    }

    private static String channelId(){
        return null;
    }

    private static final Set<String> Whitelist = new HashSet<String>(){
        {
            add("1.3.4.5");
            //add("10.6.186.49");
        }
    };
}
