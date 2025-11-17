package com.example.multi_tanent.purchases.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.multi_tanent.purchases.entity.PurPurchaseOrder;



public interface PurPurchaseOrderRepository extends JpaRepository<PurPurchaseOrder, Long> {
    Optional<PurPurchaseOrder> findByPoNumber(String poNumber);
}
