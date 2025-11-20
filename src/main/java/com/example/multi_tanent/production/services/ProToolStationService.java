package com.example.multi_tanent.production.services;

import com.example.multi_tanent.config.TenantContext;
import com.example.multi_tanent.production.dto.ProToolStationRequest;
import com.example.multi_tanent.production.dto.ProToolStationResponse;
import com.example.multi_tanent.production.entity.ProToolStation;
import com.example.multi_tanent.production.entity.ProTools;
import com.example.multi_tanent.production.repository.ProToolStationRepository;
import com.example.multi_tanent.production.repository.ProToolsRepository;
import com.example.multi_tanent.spersusers.enitity.Tenant;
import com.example.multi_tanent.spersusers.repository.TenantRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional("tenantTx")
public class ProToolStationService {

    private final ProToolStationRepository stationRepository;
    private final ProToolsRepository toolsRepository;
    private final TenantRepository tenantRepository;

    private Tenant getCurrentTenant() {
        String tenantId = TenantContext.getTenantId();
        return tenantRepository.findByTenantId(tenantId)
                .orElseThrow(() -> new IllegalStateException("Tenant not found: " + tenantId));
    }

    public ProToolStationResponse create(ProToolStationRequest request) {
        Tenant tenant = getCurrentTenant();
        ProTools tool = toolsRepository.findById(request.getToolId())
                .orElseThrow(() -> new EntityNotFoundException("Tool not found with id: " + request.getToolId()));

        ProToolStation station = ProToolStation.builder()
                .tenant(tenant)
                .tool(tool)
                .name(request.getName())
                .position(request.getPosition())
                .build();

        return toResponse(stationRepository.save(station));
    }

    @Transactional(readOnly = true)
    public List<ProToolStationResponse> getAllByToolId(Long toolId) {
        Long tenantId = getCurrentTenant().getId();
        return stationRepository.findByTenantIdAndToolId(tenantId, toolId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ProToolStationResponse update(Long id, ProToolStationRequest request) {
        Tenant tenant = getCurrentTenant();
        ProToolStation station = stationRepository.findByTenantIdAndId(tenant.getId(), id)
                .orElseThrow(() -> new EntityNotFoundException("Tool Station not found with id: " + id));

        ProTools tool = toolsRepository.findById(request.getToolId())
                .orElseThrow(() -> new EntityNotFoundException("Tool not found with id: " + request.getToolId()));

        station.setName(request.getName());
        station.setPosition(request.getPosition());
        station.setTool(tool);

        return toResponse(stationRepository.save(station));
    }

    public void delete(Long id) {
        Long tenantId = getCurrentTenant().getId();
        if (!stationRepository.existsByTenantIdAndId(tenantId, id)) {
            throw new EntityNotFoundException("Tool Station not found with id: " + id);
        }
        stationRepository.deleteById(id);
    }

    private ProToolStationResponse toResponse(ProToolStation entity) {
        return ProToolStationResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .position(entity.getPosition())
                .toolId(entity.getTool() != null ? entity.getTool().getId() : null)
                .toolName(entity.getTool() != null ? entity.getTool().getName() : null)
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
