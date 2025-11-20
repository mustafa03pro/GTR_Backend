package com.example.multi_tanent.purchases.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.multi_tanent.purchases.entity.PurchaseBill;


public interface PurchaseBillRepository extends JpaRepository<PurchaseBill, Long> {}
