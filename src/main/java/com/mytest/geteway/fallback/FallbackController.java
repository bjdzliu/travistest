package com.mytest.geteway.fallback;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class FallbackController {
    @RequestMapping(value = "/fallbackcontroller")
    public Map<String, String> fallBackController() {
        Map<String, String> res = new HashMap();
        res.put("statusCode", "9999");
        res.put("message", "[fallback]service not available");
        return res;
    }
}
