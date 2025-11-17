package com.example.multi_tanent.production.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProToolStationRequest {
    @NotBlank(message = "Station name is required.")
    private String name;

    private Integer position;

    @NotNull(message = "Tool ID is required.")
    private Long toolId;
}