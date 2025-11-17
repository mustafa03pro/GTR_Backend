package com.example.multi_tanent.production.dto;



import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProSemiFinishedGoodResponse {
    private Long id;
    private Long tenantId;

    private String itemCode;
    private String name;
    private String description;
    private String inventoryType;

    private Boolean product;
    private Boolean service;
    private Boolean purchase;
    private Boolean sales;
    private Boolean roll;
    private Boolean scrapItem;

    private Long categoryId;
    private String categoryName;
    private Long subCategoryId;
    private String subCategoryName;

    private Long issueUnitId;
    private String issueUnitName;
    private Long purchaseUnitId;
    private String purchaseUnitName;

    private BigDecimal purchaseToIssueRelation;
    private BigDecimal wastagePercent;
    private Integer reorderLimit;

    private Long priceCategoryId;
    private String priceCategoryName;
    private BigDecimal purchasePrice;
    private BigDecimal salesPrice;
    private Boolean taxInclusive;
    private Long taxId;
    private String taxName;
    private BigDecimal taxRate;

    private String imagePath;

    private Set<Long> bomItemIds;
    private Set<Long> processIds;
    private Set<Long> toolIds;
    private Set<Long> toolStationIds;

    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}

