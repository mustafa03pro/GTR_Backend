package com.example.multi_tanent.purchases.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class PurPurchaseOrderResponse {
    private Long id;
    private String poNumber;
    private String supplierName;
    private String location;
    private String poType;
    private String referenceNo;
    private LocalDate poDate;
    private String projectNumber;
    private boolean discountAtItemLevel;
    private Double subtotal;
    private Double totalDiscount;
    private Double totalTax;
    private Double grossTotal;
    private Double otherCharges;
    private Double grandTotal;
    private String deliveryTo;
    private String customerDetails;
    private String remark;
    private String emailTo;
    private String attachmentUrl; // URL to the attachment instead of raw bytes

    private List<PurPurchaseOrderItemResponse> items;
}
