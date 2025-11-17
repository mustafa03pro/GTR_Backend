package com.example.multi_tanent.purchases.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseBill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String billNo;
    private String referenceNo;
    private String supplierName;
    private String status;
    private LocalDate billDate;
    private LocalDate dueDate;
    private String location;
    private BigDecimal amount;
    private BigDecimal amountPaid;
    private BigDecimal balanceDue;
    private String notes;

    @ManyToOne
    @JoinColumn(name = "purchase_order_id")
    private PurPurchaseOrder purchaseOrder;

    // getters/setters
}
