package com.example.multi_tanent.purchases.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.multi_tanent.purchases.entity.PurPurchaseOrder;

import java.util.Optional;

public interface PurPurchaseOrderRepository extends JpaRepository<PurPurchaseOrder, Long> {
    Page<PurPurchaseOrder> findByTenantId(Long tenantId, Pageable pageable);
    Optional<PurPurchaseOrder> findByIdAndTenantId(Long id, Long tenantId);
}

