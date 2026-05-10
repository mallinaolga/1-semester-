package com.mipt.olgamallina.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PriorityStatsDto {
    private String priority;
    private long count;
}