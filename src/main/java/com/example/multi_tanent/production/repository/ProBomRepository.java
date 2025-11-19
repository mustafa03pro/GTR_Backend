package com.example.multi_tanent.production.repository;

// package com.example.multi_tanent.production.repository;

import com.example.multi_tanent.production.entity.ProBom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProBomRepository extends JpaRepository<ProBom, Long> {
    Page<ProBom> findByTenantId(Long tenantId, Pageable pageable);
    Optional<ProBom> findByIdAndTenantId(Long id, Long tenantId);
}

