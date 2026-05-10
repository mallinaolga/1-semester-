package com.mipt.olgamallina.dto;

public record ProblemDetails(
        String type,
        String title,
        int status,
        String detail
) {
}