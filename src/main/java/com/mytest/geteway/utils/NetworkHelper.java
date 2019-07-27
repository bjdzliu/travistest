package com.mytest.geteway.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;

@Slf4j
public class NetworkHelper {

    private static final String UNKNOWN = "unknown";

    /**
     * 获取Ip地址
     *
     * @param request web请求
     * @return 真实ip
     */
    public static String getRemoteIpAddr(ServerHttpRequest request) {
        HttpHeaders httpHeaders = request.getHeaders();
        String realIP = httpHeaders.getFirst("Activity-IP");

        if (StringUtils.isNotBlank(realIP) && !UNKNOWN.equalsIgnoreCase(realIP)) {
            log.info("获取到Header中Activity-IP: " + realIP);
            return realIP;
        }
        String xFor = httpHeaders.getFirst("X-FORWARDED-FOR");

        if (StringUtils.isNotBlank(xFor) && !UNKNOWN.equalsIgnoreCase(xFor)){
            return xFor;
        }

        xFor = httpHeaders.getFirst("X-REAL-IP");

        if (StringUtils.isNotBlank(xFor) && !UNKNOWN.equalsIgnoreCase(xFor)) {
            return xFor;
        }
        if (StringUtils.isBlank(xFor) || UNKNOWN.equalsIgnoreCase(xFor)) {
            xFor = httpHeaders.getFirst("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(xFor) || UNKNOWN.equalsIgnoreCase(xFor)) {
            xFor = httpHeaders.getFirst("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(xFor) || UNKNOWN.equalsIgnoreCase(xFor)) {
            xFor = httpHeaders.getFirst("HTTP_CLIENT_IP");
        }
        if (StringUtils.isBlank(xFor) || UNKNOWN.equalsIgnoreCase(xFor)) {
            xFor = httpHeaders.getFirst("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isBlank(xFor) || UNKNOWN.equalsIgnoreCase(xFor)) {
            xFor = request.getRemoteAddress().getHostString();
        }
        return xFor;
    }
}
