package com.example.multi_tanent.production.repository;



// import org.springframework.data.jpa.repository.JpaRepository;

// import com.example.multi_tanent.production.entity.ProSemiFinishedGood;

// import java.util.Optional;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.Pageable;

// public interface ProSemiFinishedGoodRepository extends JpaRepository<ProSemiFinishedGood, Long> {
//     Page<ProSemiFinishedGood> findByTenantId(Long tenantId, Pageable pageable);
//     Optional<ProSemiFinishedGood> findByIdAndTenantId(Long id, Long tenantId);
//     boolean existsByItemCodeAndTenantId(String itemCode, Long tenantId);
// }


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.multi_tanent.production.entity.ProSemiFinishedGood;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProSemiFinishedGoodRepository extends JpaRepository<ProSemiFinishedGood, Long> {
    
    Page<ProSemiFinishedGood> findByTenantId(Long tenantId, Pageable pageable);
    
    Optional<ProSemiFinishedGood> findByIdAndTenantId(Long id, Long tenantId);
    
    boolean existsByItemCodeAndTenantId(String itemCode, Long tenantId);

    // ✅ ADDED: Custom search query for Name or Item Code
    @Query("SELECT p FROM ProSemiFinishedGood p WHERE p.tenant.id = :tenantId AND " +
           "(LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.itemCode) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<ProSemiFinishedGood> searchByTenantId(@Param("tenantId") Long tenantId, 
                                               @Param("keyword") String keyword, 
                                               Pageable pageable);
}