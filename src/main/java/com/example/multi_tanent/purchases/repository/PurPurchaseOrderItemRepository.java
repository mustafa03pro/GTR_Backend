package com.example.multi_tanent.purchases.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.multi_tanent.purchases.entity.PurPurchaseOrderItem;


public interface PurPurchaseOrderItemRepository extends JpaRepository<PurPurchaseOrderItem, Long> { }
