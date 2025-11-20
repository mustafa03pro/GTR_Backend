package com.example.multi_tanent.production.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProBomItemMaterialResponse {
    private Long id;
    private Long rawMaterialId;
    private String rawMaterialName;

    private Long unitId;
    private String unitName;

    private BigDecimal quantity;
    private String notes;
}


