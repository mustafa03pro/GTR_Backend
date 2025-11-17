package com.example.multi_tanent.purchases.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurPurchaseOrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String category;
    private String subcategory;
    private String itemCode;
    private String itemName;
    private int quantity;
    private double price;
    private double discount; // %
    private double tax;      // %
    private double total;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_order_id")
    @JsonBackReference
    private PurPurchaseOrder purchaseOrder;

    // getters/setters
}

