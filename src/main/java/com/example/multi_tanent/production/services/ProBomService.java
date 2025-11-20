package com.example.multi_tanent.production.services;


import com.example.multi_tanent.config.TenantContext;
import com.example.multi_tanent.production.dto.*;
import com.example.multi_tanent.production.entity.*;
import com.example.multi_tanent.production.repository.ProBomRepository;
import com.example.multi_tanent.production.repository.ProRawMaterialsRepository;
import com.example.multi_tanent.production.repository.ProProcessRepository;
import com.example.multi_tanent.spersusers.enitity.Tenant;
import com.example.multi_tanent.spersusers.repository.TenantRepository;
import com.example.multi_tanent.production.repository.ProCategoryRepository;
import com.example.multi_tanent.production.repository.ProSubCategoryRepository;
import com.example.multi_tanent.production.repository.ProUnitRepository;
import com.example.multi_tanent.production.repository.ProSemiFinishedGoodRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional("tenantTx")
public class ProBomService {

    private final ProBomRepository bomRepo;
    private final TenantRepository tenantRepo;

    // Repos required to resolve references
    private final ProSemiFinishedGoodRepository sfgRepo;
    private final ProProcessRepository processRepo;
    private final ProCategoryRepository categoryRepo;
    private final ProSubCategoryRepository subCategoryRepo;
    private final ProRawMaterialsRepository rawMaterialRepo;
    private final ProUnitRepository unitRepo;

    private Tenant currentTenant() {
        String key = TenantContext.getTenantId();
        return tenantRepo.findFirstByOrderByIdAsc()
                .orElseThrow(() -> new IllegalStateException("Tenant not resolved for key: " + key));
    }

    public ProBomResponse create(ProBomRequest req) {
        Tenant t = currentTenant();

        ProSemiFinishedGood product = sfgRepo.findById(req.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found: " + req.getProductId()));

        ProBom bom = new ProBom();
        bom.setTenant(t);
        bom.setProduct(product);
        bom.setBomName(req.getBomName());
        bom.setLocked(Optional.ofNullable(req.getLocked()).orElse(Boolean.FALSE));
        bom.setCreatedBy(req.getCreatedBy());
        bom.setCreatedAt(LocalDateTime.now());

        // if copyFromBomId provided, set reference (read-only copy relationship)
        if (req.getCopyFromBomId() != null) {
            ProBom copyFrom = bomRepo.findById(req.getCopyFromBomId())
                    .orElseThrow(() -> new EntityNotFoundException("Source BOM not found: " + req.getCopyFromBomId()));
            bom.setCopyFromBom(copyFrom);
        }

        // map items if provided
        if (req.getItems() != null) {
            List<ProBomItem> items = req.getItems().stream().map(this::mapItemRequestToEntity).collect(Collectors.toList());
            bom.setItems(items);
            // ensure parent pointers set by setItems method in entity
        }

        ProBom saved = bomRepo.save(bom);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public Page<ProBomResponse> list(Pageable pageable) {
        Tenant t = currentTenant();
        return bomRepo.findByTenantId(t.getId(), pageable).map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public ProBomResponse getById(Long id) {
        Tenant t = currentTenant();
        ProBom bom = bomRepo.findByIdAndTenantId(id, t.getId())
                .orElseThrow(() -> new EntityNotFoundException("BOM not found: " + id));
        return toResponse(bom);
    }

    public ProBomResponse update(Long id, ProBomRequest req) {
        Tenant t = currentTenant();
        ProBom bom = bomRepo.findByIdAndTenantId(id, t.getId())
                .orElseThrow(() -> new EntityNotFoundException("BOM not found: " + id));

        if (req.getProductId() != null) {
            ProSemiFinishedGood product = sfgRepo.findById(req.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found: " + req.getProductId()));
            bom.setProduct(product);
        }

        if (req.getBomName() != null) bom.setBomName(req.getBomName());
        if (req.getLocked() != null) bom.setLocked(req.getLocked());
        if (req.getCopyFromBomId() != null) {
            ProBom copyFrom = bomRepo.findById(req.getCopyFromBomId())
                    .orElseThrow(() -> new EntityNotFoundException("Source BOM not found: " + req.getCopyFromBomId()));
            bom.setCopyFromBom(copyFrom);
        } else {
            bom.setCopyFromBom(null);
        }
        if (req.getCreatedBy() != null) bom.setCreatedBy(req.getCreatedBy());

        // update items: replace when provided (otherwise keep existing)
        if (req.getItems() != null) {
            List<ProBomItem> items = req.getItems().stream().map(this::mapItemRequestToEntity).collect(Collectors.toList());
            bom.setItems(items);
        }

        ProBom updated = bomRepo.save(bom);
        return toResponse(updated);
    }

    public void delete(Long id) {
        Tenant t = currentTenant();
        ProBom bom = bomRepo.findByIdAndTenantId(id, t.getId())
                .orElseThrow(() -> new EntityNotFoundException("BOM not found: " + id));
        bomRepo.delete(bom);
    }

    /* ---------------- mapping helpers ---------------- */

    private ProBomItem mapItemRequestToEntity(ProBomItemRequest r) {
        ProBomItem item = new ProBomItem();
        item.setLineNumber(r.getLineNumber());

        if (r.getProcessId() != null) {
            ProProcess p = processRepo.findById(r.getProcessId())
                    .orElseThrow(() -> new EntityNotFoundException("Process not found: " + r.getProcessId()));
            item.setProcess(p);
        } else {
            item.setProcess(null);
        }

        if (r.getCategoryId() != null) {
            item.setCategory(categoryRepo.findById(r.getCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Category not found: " + r.getCategoryId())));
        } else {
            item.setCategory(null);
        }

        if (r.getSubCategoryId() != null) {
            item.setSubCategory(subCategoryRepo.findById(r.getSubCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException("SubCategory not found: " + r.getSubCategoryId())));
        } else {
            item.setSubCategory(null);
        }

        if (r.getMaterials() != null) {
            List<ProBomItemMaterial> mats = r.getMaterials().stream()
                    .map(this::mapMaterialRequestToEntity)
                    .collect(Collectors.toList());
            item.setMaterials(mats);
        }

        return item;
    }

    private ProBomItemMaterial mapMaterialRequestToEntity(ProBomItemMaterialRequest r) {
        ProBomItemMaterial m = new ProBomItemMaterial();

        ProRawMaterials rm = rawMaterialRepo.findById(r.getRawMaterialId())
                .orElseThrow(() -> new EntityNotFoundException("Raw material not found: " + r.getRawMaterialId()));
        m.setRawMaterial(rm);

        if (r.getUnitId() != null) {
            m.setUnit(unitRepo.findById(r.getUnitId())
                    .orElseThrow(() -> new EntityNotFoundException("Unit not found: " + r.getUnitId())));
        }

        m.setQuantity(r.getQuantity());
        m.setNotes(r.getNotes());
        return m;
    }

    private ProBomResponse toResponse(ProBom bom) {
        ProBomResponse.ProBomResponseBuilder rb = ProBomResponse.builder()
                .id(bom.getId())
                .tenantId(bom.getTenant() != null ? bom.getTenant().getId() : null)
                .productId(bom.getProduct() != null ? bom.getProduct().getId() : null)
                .productName(bom.getProduct() != null ? bom.getProduct().getName() : null)
                .bomName(bom.getBomName())
                .locked(bom.getLocked())
                .copyFromBomId(bom.getCopyFromBom() != null ? bom.getCopyFromBom().getId() : null)
                .copyFromBomName(bom.getCopyFromBom() != null ? bom.getCopyFromBom().getBomName() : null)
                .createdBy(bom.getCreatedBy())
                .createdAt(bom.getCreatedAt());

        List<ProBomItemResponse> items = bom.getItems() == null ? Collections.emptyList()
                : bom.getItems().stream().map(this::toItemResponse).collect(Collectors.toList());

        rb.items(items);
        return rb.build();
    }

    private ProBomItemResponse toItemResponse(ProBomItem item) {
        ProBomItemResponse.ProBomItemResponseBuilder ib = ProBomItemResponse.builder()
                .id(item.getId())
                .lineNumber(item.getLineNumber())
                .processId(item.getProcess() != null ? item.getProcess().getId() : null)
                .processName(item.getProcess() != null ? item.getProcess().getName() : null)
                .categoryId(item.getCategory() != null ? item.getCategory().getId() : null)
                .categoryName(item.getCategory() != null ? item.getCategory().getName() : null)
                .subCategoryId(item.getSubCategory() != null ? item.getSubCategory().getId() : null)
                .subCategoryName(item.getSubCategory() != null ? item.getSubCategory().getName() : null);

        List<ProBomItemMaterialResponse> mats = item.getMaterials() == null ? Collections.emptyList()
                : item.getMaterials().stream().map(this::toMaterialResponse).collect(Collectors.toList());

        ib.materials(mats);
        return ib.build();
    }

    private ProBomItemMaterialResponse toMaterialResponse(ProBomItemMaterial m) {
        return ProBomItemMaterialResponse.builder()
                .id(m.getId())
                .rawMaterialId(m.getRawMaterial() != null ? m.getRawMaterial().getId() : null)
                .rawMaterialName(m.getRawMaterial() != null ? m.getRawMaterial().getName() : null)
                .unitId(m.getUnit() != null ? m.getUnit().getId() : null)
                .unitName(m.getUnit() != null ? m.getUnit().getName() : null)
                .quantity(m.getQuantity())
                .notes(m.getNotes())
                .build();
    }
}

