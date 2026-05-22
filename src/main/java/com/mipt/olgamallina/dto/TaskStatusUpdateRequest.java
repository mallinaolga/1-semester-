package com.mipt.olgamallina.dto;

import jakarta.validation.constraints.NotNull;

public record TaskStatusUpdateRequest(
        @NotNull Boolean completed
) {
}