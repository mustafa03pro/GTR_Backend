package com.example.multi_tanent.purchases.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchasePayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String paymentNumber;
    private LocalDate paymentDate;
    private String supplierName;
    private Double amount;
    private String paymentMode; // e.g. Card, Cash
    private boolean taxDeducted;
    private String referenceNo;
    private String chequeNumber;
    private String paidThrough;
    private Double tds;
    private Double advanceAmount;
    private String notes;

    @ManyToOne
    @JoinColumn(name = "bill_id")
    private PurchaseBill bill;

    // getters/setters
}
