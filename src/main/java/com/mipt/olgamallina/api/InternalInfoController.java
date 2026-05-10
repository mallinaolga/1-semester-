package com.mipt.olgamallina.api;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class InternalInfoController {

    @GetMapping("/profile")
    public Map<String, Object> profile(Authentication authentication) {
        return Map.of(
                "username", authentication.getName(),
                "authorities", authentication.getAuthorities()
                        .stream()
                        .map(Object::toString)
                        .toList()
        );
    }

    @GetMapping("/docs")
    public Map<String, String> docs() {
        return Map.of(
                "message",
                "Sensitive docs visible only with READ_PRIVILEGE"
        );
    }
}