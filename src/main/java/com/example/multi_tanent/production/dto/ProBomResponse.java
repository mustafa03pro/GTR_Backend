package com.example.multi_tanent.production.dto;

// package com.example.multi_tanent.production.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProBomResponse {
    private Long id;
    private Long tenantId;

    private Long productId;
    private String productName;

    private String bomName;
    private Boolean locked;
    private Long copyFromBomId;
    private String copyFromBomName;

    private String createdBy;
    private LocalDateTime createdAt;

    private List<ProBomItemResponse> items;
}



