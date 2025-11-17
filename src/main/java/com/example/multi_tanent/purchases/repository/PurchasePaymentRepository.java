package com.example.multi_tanent.purchases.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.multi_tanent.purchases.entity.PurchasePayment;



public interface PurchasePaymentRepository extends JpaRepository<PurchasePayment, Long> {}