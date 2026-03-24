package com.mipt.olgamallina.service;

import org.springframework.stereotype.Service;

@Service
public class PreferencesService {

    public static final String DEFAULT_MODE = "compact";

    public String normalizeMode(String mode) {
        if (mode == null || mode.isBlank()) {
            return DEFAULT_MODE;
        }

        String normalized = mode.trim().toLowerCase();
        if (!normalized.equals("compact") && !normalized.equals("detailed")) {
            return DEFAULT_MODE;
        }
        return normalized;
    }
}