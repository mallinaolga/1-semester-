package com.mipt.olgamallina.controller;

import com.mipt.olgamallina.dto.ViewPreferenceResponseDto;
import com.mipt.olgamallina.service.PreferencesService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/preferences")
public class PreferencesController {

    private static final String COOKIE_NAME = "viewPreference";

    private final PreferencesService preferencesService;

    public PreferencesController(PreferencesService preferencesService) {
        this.preferencesService = preferencesService;
    }

    @Operation(summary = "Read view mode from cookie")
    @GetMapping("/view")
    public ResponseEntity<ViewPreferenceResponseDto> getViewPreference(
            @CookieValue(value = COOKIE_NAME, required = false) String mode,
            HttpServletResponse response) {

        String normalized = preferencesService.normalizeMode(mode);

        if (mode == null) {
            ResponseCookie cookie = ResponseCookie.from(COOKIE_NAME, normalized)
                    .path("/")
                    .httpOnly(false)
                    .build();
            response.addHeader("Set-Cookie", cookie.toString());
        }

        return ResponseEntity.ok(new ViewPreferenceResponseDto(normalized));
    }

    @Operation(summary = "Update view mode cookie")
    @PostMapping("/view")
    public ResponseEntity<ViewPreferenceResponseDto> updateViewPreference(
            @RequestParam String mode,
            HttpServletResponse response) {

        String normalized = preferencesService.normalizeMode(mode);

        ResponseCookie cookie = ResponseCookie.from(COOKIE_NAME, normalized)
                .path("/")
                .httpOnly(false)
                .build();
        response.addHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.ok(new ViewPreferenceResponseDto(normalized));
    }
}