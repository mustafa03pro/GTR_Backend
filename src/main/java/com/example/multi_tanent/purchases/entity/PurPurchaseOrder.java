package com.example.multi_tanent.purchases.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurPurchaseOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String poNumber;       // auto-generated like PO-0001
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

    @Lob
    private byte[] attachment;

    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<PurPurchaseOrderItem> items;
    // getters/setters
}

