package com.example.multi_tanent.purchases.dto;

import lombok.Data;

@Data
public class PurPurchaseOrderItemRequest {
    private String category;
    private String subcategory;
    private String itemCode;
    private String itemName;
    private int quantity;
    private double price;
    private double discount;
    private double tax;
    private double total;
}
