package com.example.multi_tanent.production.dto;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProSemiFinishedGoodRequest {
    @NotBlank
    private String name;

    private String itemCode;
    private String description;

    private String inventoryType;

    private Boolean product;
    private Boolean service;
    private Boolean purchase;
    private Boolean sales;
    private Boolean roll;
    private Boolean scrapItem;

    private Long categoryId;
    private Long subCategoryId;

    private Long issueUnitId;
    private Long purchaseUnitId;
    private BigDecimal purchaseToIssueRelation;
    private BigDecimal wastagePercent;
    private Integer reorderLimit;

    private Long priceCategoryId;
    private BigDecimal purchasePrice;
    private BigDecimal salesPrice;
    private Boolean taxInclusive;
    private Long taxId;
    private BigDecimal taxRate;

    private String imagePath;

    // Associations as sets of ids
    private Set<Long> bomItemIds;       // if you want to link existing raw materials
    private Set<Long> processIds;
    private Set<Long> toolIds;
    private Set<Long> toolStationIds;
}

