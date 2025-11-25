package com.example.multi_tanent.purchases.repository;

import com.example.multi_tanent.purchases.entity.PurPurchaseInvoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PurPurchaseInvoiceRepository extends JpaRepository<PurPurchaseInvoice, Long> {
    Page<PurPurchaseInvoice> findByTenantId(Long tenantId, Pageable pageable);
    Optional<PurPurchaseInvoice> findByIdAndTenantId(Long id, Long tenantId);
}
