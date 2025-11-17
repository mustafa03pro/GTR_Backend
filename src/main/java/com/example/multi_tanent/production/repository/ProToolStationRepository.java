package com.example.multi_tanent.production.repository;

import com.example.multi_tanent.production.entity.ProToolStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProToolStationRepository extends JpaRepository<ProToolStation, Long> {
    List<ProToolStation> findByTenantIdAndToolId(Long tenantId, Long toolId);
    Optional<ProToolStation> findByTenantIdAndId(Long tenantId, Long id);
    boolean existsByTenantIdAndId(Long tenantId, Long id);
}