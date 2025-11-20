package com.example.multi_tanent.purchases.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PurchasePaymentRequest {
    private LocalDate paymentDate;
    private String supplierName;
    private Double amount;
    private String paymentMode;
    private boolean taxDeducted;
    private String referenceNo;
    private String chequeNumber;
    private String paidThrough;
    private Double tds;
    private Double advanceAmount;
    private String notes;
    private Long billId;
}
