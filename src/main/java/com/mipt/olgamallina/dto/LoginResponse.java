package com.mipt.olgamallina.dto;

import java.time.Instant;
import java.util.List;

public record LoginResponse(
        String tokenType,
        String accessToken,
        Instant expiresAt,
        String username,
        List<String> authorities
) {
}