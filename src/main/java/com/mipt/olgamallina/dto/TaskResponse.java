package com.mipt.olgamallina.dto;

public record TaskResponse(
        Long id,
        String title,
        String description,
        boolean completed
) {
}