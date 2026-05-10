package com.mipt.olgamallina.dto;

import jakarta.validation.constraints.NotBlank;

public record TaskCreateRequest(
        @NotBlank String title,
        String description
) {
}