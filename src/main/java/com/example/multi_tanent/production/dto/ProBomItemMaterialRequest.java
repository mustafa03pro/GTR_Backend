package com.example.multi_tanent.production.dto;
import lombok.*;

import java.math.BigDecimal;


import jakarta.validation.constraints.DecimalMin;

import jakarta.validation.constraints.NotNull;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProBomItemMaterialRequest {
    @NotNull
    private Long rawMaterialId;

    private Long unitId;

    @NotNull @DecimalMin("0.000001")
    private BigDecimal quantity;

    private String notes;
}


