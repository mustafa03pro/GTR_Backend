package com.example.multi_tanent.purchases.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PurchaseBillRequest {
    private String billNo;
    private String referenceNo;
    private String supplierName;
    private String status;
    private LocalDate billDate;
    private LocalDate dueDate;
    private String location;
    private BigDecimal amount;
    private String notes;

    private Long purchaseOrderId;
}
