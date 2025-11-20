package com.example.multi_tanent.production.dto;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class ProToolStationResponse {
    private Long id;
    private String name;
    private Integer position;
    private Long toolId;
    private String toolName;
    private OffsetDateTime createdAt;
}
