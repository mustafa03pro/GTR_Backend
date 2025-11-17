// package com.example.multi_tanent.production.services;

// import com.example.multi_tanent.config.TenantContext;
// import com.example.multi_tanent.production.dto.ProSemiFinishedGoodRequest;
// import com.example.multi_tanent.production.dto.ProSemiFinishedGoodResponse;
// import com.example.multi_tanent.production.entity.ProProcess;
// import com.example.multi_tanent.production.entity.ProRawMaterials;
// import com.example.multi_tanent.production.entity.ProSemiFinishedGood;
// import com.example.multi_tanent.production.entity.ProToolStation;
// import com.example.multi_tanent.production.entity.ProTools;
// import com.example.multi_tanent.production.repository.ProCategoryRepository;
// import com.example.multi_tanent.production.repository.ProPriceCategoryRepository;
// import com.example.multi_tanent.production.repository.ProProcessRepository;
// import com.example.multi_tanent.production.repository.ProRawMaterialsRepository;
// import com.example.multi_tanent.production.repository.ProSemiFinishedGoodRepository;
// import com.example.multi_tanent.production.repository.ProSubCategoryRepository;
// import com.example.multi_tanent.production.repository.ProTaxRepository;
// import com.example.multi_tanent.production.repository.ProToolStationRepository;
// import com.example.multi_tanent.production.repository.ProToolsRepository;
// import com.example.multi_tanent.production.repository.ProUnitRepository;
// import com.example.multi_tanent.spersusers.enitity.Tenant;
// import com.example.multi_tanent.spersusers.repository.TenantRepository;

// import jakarta.persistence.EntityNotFoundException;
// import lombok.RequiredArgsConstructor;
// import org.springframework.data.domain.*;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

// import java.util.*;
// import java.util.stream.Collectors;

// @Service
// @RequiredArgsConstructor
// @Transactional("tenantTx")
// public class ProSemiFinishedGoodService {

//     private final ProSemiFinishedGoodRepository repo;
//     private final TenantRepository tenantRepo;

//     // Repos for related entities (assume they exist)
//     private final ProCategoryRepository categoryRepo;
//     private final ProSubCategoryRepository subCategoryRepo;
//     private final ProUnitRepository unitRepo;
//     private final ProPriceCategoryRepository priceCategoryRepo;
//     private final ProTaxRepository taxRepo;
//     private final ProRawMaterialsRepository rawMaterialRepo;
//     private final ProProcessRepository processRepo;
//     private final ProToolsRepository toolRepo;
//     private final ProToolStationRepository toolStationRepo;

//     private Tenant currentTenant() {
//         // same approach as other services in your project
//         String key = TenantContext.getTenantId();
//         return tenantRepo.findFirstByOrderByIdAsc()
//                 .orElseThrow(() -> new IllegalStateException("Tenant not resolved for key: " + key));
//     }

//     public ProSemiFinishedGoodResponse create(ProSemiFinishedGoodRequest req) {
//         Tenant t = currentTenant();

//         ProSemiFinishedGood e = ProSemiFinishedGood.builder()
//                 .tenant(t)
//                 .itemCode(req.getItemCode())
//                 .name(req.getName())
//                 .description(req.getDescription())
//                 .inventoryType(req.getInventoryType())
//                 .product(Optional.ofNullable(req.getProduct()).orElse(Boolean.TRUE))
//                 .service(req.getService())
//                 .purchase(req.getPurchase())
//                 .sales(req.getSales())
//                 .roll(req.getRoll())
//                 .scrapItem(req.getScrapItem())
//                 .purchaseToIssueRelation(Optional.ofNullable(req.getPurchaseToIssueRelation()).orElse(BigDecimal.ONE))
//                 .wastagePercent(req.getWastagePercent())
//                 .reorderLimit(req.getReorderLimit())
//                 .purchasePrice(req.getPurchasePrice())
//                 .salesPrice(req.getSalesPrice())
//                 .taxInclusive(Optional.ofNullable(req.getTaxInclusive()).orElse(Boolean.FALSE))
//                 .taxRate(req.getTaxRate())
//                 .imagePath(req.getImagePath())
//                 .build();

//         // set category/subcategory/units/price/tax
//         if (req.getCategoryId() != null) {
//             e.setCategory(categoryRepo.findById(req.getCategoryId())
//                     .orElseThrow(() -> new EntityNotFoundException("Category not found: " + req.getCategoryId())));
//         }
//         if (req.getSubCategoryId() != null) {
//             e.setSubCategory(subCategoryRepo.findById(req.getSubCategoryId())
//                     .orElseThrow(() -> new EntityNotFoundException("SubCategory not found: " + req.getSubCategoryId())));
//         }
//         if (req.getIssueUnitId() != null) {
//             e.setIssueUnit(unitRepo.findById(req.getIssueUnitId())
//                     .orElseThrow(() -> new EntityNotFoundException("Issue unit not found: " + req.getIssueUnitId())));
//         }
//         if (req.getPurchaseUnitId() != null) {
//             e.setPurchaseUnit(unitRepo.findById(req.getPurchaseUnitId())
//                     .orElseThrow(() -> new EntityNotFoundException("Purchase unit not found: " + req.getPurchaseUnitId())));
//         }
//         if (req.getPriceCategoryId() != null) {
//             e.setPriceCategory(priceCategoryRepo.findById(req.getPriceCategoryId())
//                     .orElseThrow(() -> new EntityNotFoundException("Price category not found: " + req.getPriceCategoryId())));
//         }
//         if (req.getTaxId() != null) {
//             e.setTax(taxRepo.findById(req.getTaxId())
//                     .orElseThrow(() -> new EntityNotFoundException("Tax not found: " + req.getTaxId())));
//         }

//         // Many-to-many and BOM sets
//         if (req.getBomItemIds() != null) {
//             Set<ProRawMaterials> boms = new LinkedHashSet<>(rawMaterialRepo.findAllById(req.getBomItemIds()));
//             e.getBomItems().clear();
//             e.getBomItems().addAll(boms);
//         }

//         if (req.getProcessIds() != null) {
//             Set<ProProcess> procs = new LinkedHashSet<>(processRepo.findAllById(req.getProcessIds()));
//             e.getProcesses().clear();
//             e.getProcesses().addAll(procs);
//         }

//         if (req.getToolIds() != null) {
//             Set<ProTools> tools = new LinkedHashSet<>(toolRepo.findAllById(req.getToolIds()));
//             e.getTools().clear();
//             e.getTools().addAll(tools);
//         }

//         if (req.getToolStationIds() != null) {
//             Set<ProToolStation> stations = new LinkedHashSet<>(toolStationRepo.findAllById(req.getToolStationIds()));
//             e.getToolStations().clear();
//             e.getToolStations().addAll(stations);
//         }

//         ProSemiFinishedGood saved = repo.save(e);
//         return toResponse(saved);
//     }

//     @Transactional(readOnly = true)
//     public Page<ProSemiFinishedGoodResponse> findAll(Pageable pageable) {
//         Tenant t = currentTenant();
//         return repo.findByTenantId(t.getId(), pageable).map(this::toResponse);
//     }

//     @Transactional(readOnly = true)
//     public ProSemiFinishedGoodResponse getById(Long id) {
//         Tenant t = currentTenant();
//         ProSemiFinishedGood e = repo.findByIdAndTenantId(id, t.getId())
//                 .orElseThrow(() -> new EntityNotFoundException("Semi finished good not found: " + id));
//         return toResponse(e);
//     }

//     public ProSemiFinishedGoodResponse update(Long id, ProSemiFinishedGoodRequest req) {
//         Tenant t = currentTenant();
//         ProSemiFinishedGood e = repo.findByIdAndTenantId(id, t.getId())
//                 .orElseThrow(() -> new EntityNotFoundException("Semi finished good not found: " + id));

//         // update simple fields if present
//         if (req.getName() != null) e.setName(req.getName());
//         if (req.getItemCode() != null) e.setItemCode(req.getItemCode());
//         if (req.getDescription() != null) e.setDescription(req.getDescription());
//         if (req.getInventoryType() != null) e.setInventoryType(req.getInventoryType());
//         if (req.getProduct() != null) e.setProduct(req.getProduct());
//         if (req.getService() != null) e.setService(req.getService());
//         if (req.getPurchase() != null) e.setPurchase(req.getPurchase());
//         if (req.getSales() != null) e.setSales(req.getSales());
//         if (req.getRoll() != null) e.setRoll(req.getRoll());
//         if (req.getScrapItem() != null) e.setScrapItem(req.getScrapItem());
//         if (req.getPurchaseToIssueRelation() != null) e.setPurchaseToIssueRelation(req.getPurchaseToIssueRelation());
//         if (req.getWastagePercent() != null) e.setWastagePercent(req.getWastagePercent());
//         if (req.getReorderLimit() != null) e.setReorderLimit(req.getReorderLimit());
//         if (req.getPurchasePrice() != null) e.setPurchasePrice(req.getPurchasePrice());
//         if (req.getSalesPrice() != null) e.setSalesPrice(req.getSalesPrice());
//         if (req.getTaxInclusive() != null) e.setTaxInclusive(req.getTaxInclusive());
//         if (req.getTaxRate() != null) e.setTaxRate(req.getTaxRate());
//         if (req.getImagePath() != null) e.setImagePath(req.getImagePath());

//         // relations
//         if (req.getCategoryId() != null) {
//             e.setCategory(categoryRepo.findById(req.getCategoryId())
//                     .orElseThrow(() -> new EntityNotFoundException("Category not found: " + req.getCategoryId())));
//         }
//         if (req.getSubCategoryId() != null) {
//             e.setSubCategory(subCategoryRepo.findById(req.getSubCategoryId())
//                     .orElseThrow(() -> new EntityNotFoundException("SubCategory not found: " + req.getSubCategoryId())));
//         }
//         if (req.getIssueUnitId() != null) {
//             e.setIssueUnit(unitRepo.findById(req.getIssueUnitId())
//                     .orElseThrow(() -> new EntityNotFoundException("Issue unit not found: " + req.getIssueUnitId())));
//         }
//         if (req.getPurchaseUnitId() != null) {
//             e.setPurchaseUnit(unitRepo.findById(req.getPurchaseUnitId())
//                     .orElseThrow(() -> new EntityNotFoundException("Purchase unit not found: " + req.getPurchaseUnitId())));
//         }
//         if (req.getPriceCategoryId() != null) {
//             e.setPriceCategory(priceCategoryRepo.findById(req.getPriceCategoryId())
//                     .orElseThrow(() -> new EntityNotFoundException("Price category not found: " + req.getPriceCategoryId())));
//         }
//         if (req.getTaxId() != null) {
//             e.setTax(taxRepo.findById(req.getTaxId())
//                     .orElseThrow(() -> new EntityNotFoundException("Tax not found: " + req.getTaxId())));
//         }

//         // update sets (replace when provided)
//         if (req.getBomItemIds() != null) {
//             Set<ProRawMaterials> boms = new LinkedHashSet<>(rawMaterialRepo.findAllById(req.getBomItemIds()));
//             e.getBomItems().addAll(boms);
//         } else {
//             e.getBomItems().clear();
//         }

//         if (req.getProcessIds() != null) {
//             Set<ProProcess> procs = new LinkedHashSet<>(processRepo.findAllById(req.getProcessIds()));
//             e.getProcesses().addAll(procs);
//         } else {
//             e.getProcesses().clear();
//         }

//         if (req.getToolIds() != null) {
//             Set<ProTools> tools = new LinkedHashSet<>(toolRepo.findAllById(req.getToolIds()));
//             e.getTools().addAll(tools);
//         } else {
//             e.getTools().clear();
//         }

//         if (req.getToolStationIds() != null) {
//             Set<ProToolStation> stations = new LinkedHashSet<>(toolStationRepo.findAllById(req.getToolStationIds()));
//             e.getToolStations().addAll(stations);
//         } else {
//             e.getToolStations().clear();
//         }

//         ProSemiFinishedGood updated = repo.save(e);
//         return toResponse(updated);
//     }

//     public void delete(Long id) {
//         Tenant t = currentTenant();
//         ProSemiFinishedGood e = repo.findByIdAndTenantId(id, t.getId())
//                 .orElseThrow(() -> new EntityNotFoundException("Semi finished good not found: " + id));
//         repo.delete(e);
//     }

//     /* ---------- mapping helper ---------- */

//     private ProSemiFinishedGoodResponse toResponse(ProSemiFinishedGood e) {
//         ProSemiFinishedGoodResponse.ProSemiFinishedGoodResponseBuilder b = ProSemiFinishedGoodResponse.builder()
//                 .id(e.getId())
//                 .tenantId(e.getTenant() != null ? e.getTenant().getId() : null)
//                 .itemCode(e.getItemCode())
//                 .name(e.getName())
//                 .description(e.getDescription())
//                 .inventoryType(e.getInventoryType())
//                 .product(e.getProduct())
//                 .service(e.getService())
//                 .purchase(e.getPurchase())
//                 .sales(e.getSales())
//                 .roll(e.getRoll())
//                 .scrapItem(e.getScrapItem())
//                 .purchaseToIssueRelation(e.getPurchaseToIssueRelation())
//                 .wastagePercent(e.getWastagePercent())
//                 .reorderLimit(e.getReorderLimit())
//                 .purchasePrice(e.getPurchasePrice())
//                 .salesPrice(e.getSalesPrice())
//                 .taxInclusive(e.getTaxInclusive())
//                 .taxRate(e.getTaxRate())
//                 .imagePath(e.getImagePath())
//                 .createdAt(e.getCreatedAt())
//                 .updatedAt(e.getUpdatedAt());

//         if (e.getCategory() != null) {
//             b.categoryId(e.getCategory().getId()).categoryName(e.getCategory().getName());
//         }
//         if (e.getSubCategory() != null) {
//             b.subCategoryId(e.getSubCategory().getId()).subCategoryName(e.getSubCategory().getName());
//         }
//         if (e.getIssueUnit() != null) {
//             b.issueUnitId(e.getIssueUnit().getId()).issueUnitName(e.getIssueUnit().getName());
//         }
//         if (e.getPurchaseUnit() != null) {
//             b.purchaseUnitId(e.getPurchaseUnit().getId()).purchaseUnitName(e.getPurchaseUnit().getName());
//         }
//         if (e.getPriceCategory() != null) {
//             b.priceCategoryId(e.getPriceCategory().getId()).priceCategoryName(e.getPriceCategory().getName());
//         }
//         if (e.getTax() != null) {
//             b.taxId(e.getTax().getId()).taxName(e.getTax().getName());
//         }

//         b.bomItemIds(e.getBomItems().stream().map(ProRawMaterials::getId).collect(Collectors.toSet()));
//         b.processIds(e.getProcesses().stream().map(ProProcess::getId).collect(Collectors.toSet()));
//         b.toolIds(e.getTools().stream().map(ProTools::getId).collect(Collectors.toSet()));
//         b.toolStationIds(e.getToolStations().stream().map(ProToolStation::getId).collect(Collectors.toSet()));

//         return b.build();
//     }
// }

package com.example.multi_tanent.production.services;

// import com.example.multi_tanent.config.TenantContext;
// import com.example.multi_tanent.production.dto.ProSemiFinishedGoodRequest;
// import com.example.multi_tanent.production.dto.ProSemiFinishedGoodResponse;
// import com.example.multi_tanent.production.entity.ProProcess;
// import com.example.multi_tanent.production.entity.ProRawMaterials;
// import com.example.multi_tanent.production.entity.ProSemiFinishedGood;
// import com.example.multi_tanent.production.entity.ProToolStation;
// import com.example.multi_tanent.production.entity.ProTools;
// import com.example.multi_tanent.production.repository.ProCategoryRepository;
// import com.example.multi_tanent.production.repository.ProPriceCategoryRepository;
// import com.example.multi_tanent.production.repository.ProProcessRepository;
// import com.example.multi_tanent.production.repository.ProRawMaterialsRepository;
// import com.example.multi_tanent.production.repository.ProSemiFinishedGoodRepository;
// import com.example.multi_tanent.production.repository.ProSubCategoryRepository;
// import com.example.multi_tanent.production.repository.ProTaxRepository;
// import com.example.multi_tanent.production.repository.ProToolStationRepository;
// import com.example.multi_tanent.production.repository.ProToolsRepository;
// import com.example.multi_tanent.production.repository.ProUnitRepository;
// import com.example.multi_tanent.spersusers.enitity.Tenant;
// import com.example.multi_tanent.spersusers.repository.TenantRepository;

// import jakarta.persistence.EntityNotFoundException;
// import lombok.RequiredArgsConstructor;
// import org.springframework.data.domain.*;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

// import java.math.BigDecimal;
// import java.util.*;
// import java.util.stream.Collectors;

// @Service
// @RequiredArgsConstructor
// @Transactional("tenantTx")
// public class ProSemiFinishedGoodService {

//     private final ProSemiFinishedGoodRepository repo;
//     private final TenantRepository tenantRepo;

//     // Repos for related entities (assume they exist)
//     private final ProCategoryRepository categoryRepo;
//     private final ProSubCategoryRepository subCategoryRepo;
//     private final ProUnitRepository unitRepo;
//     private final ProPriceCategoryRepository priceCategoryRepo;
//     private final ProTaxRepository taxRepo;
//     private final ProRawMaterialsRepository rawMaterialRepo;
//     private final ProProcessRepository processRepo;
//     private final ProToolsRepository toolRepo;
//     private final ProToolStationRepository toolStationRepo;

//     private Tenant currentTenant() {
//         String key = TenantContext.getTenantId();
//         return tenantRepo.findFirstByOrderByIdAsc()
//                 .orElseThrow(() -> new IllegalStateException("Tenant not resolved for key: " + key));
//     }

//     public ProSemiFinishedGoodResponse create(ProSemiFinishedGoodRequest req) {
//         Tenant t = currentTenant();

//         ProSemiFinishedGood e = ProSemiFinishedGood.builder()
//                 .tenant(t)
//                 .itemCode(req.getItemCode())
//                 .name(req.getName())
//                 .description(req.getDescription())
//                 .inventoryType(req.getInventoryType())
//                 .product(Optional.ofNullable(req.getProduct()).orElse(Boolean.TRUE))
//                 .service(req.getService())
//                 .purchase(req.getPurchase())
//                 .sales(req.getSales())
//                 .roll(req.getRoll())
//                 .scrapItem(req.getScrapItem())
//                 .purchaseToIssueRelation(Optional.ofNullable(req.getPurchaseToIssueRelation()).orElse(BigDecimal.ONE))
//                 .wastagePercent(req.getWastagePercent())
//                 .reorderLimit(req.getReorderLimit())
//                 .purchasePrice(req.getPurchasePrice())
//                 .salesPrice(req.getSalesPrice())
//                 .taxInclusive(Optional.ofNullable(req.getTaxInclusive()).orElse(Boolean.FALSE))
//                 .taxRate(req.getTaxRate())
//                 .imagePath(req.getImagePath())
//                 .build();

//         // set category/subcategory/units/price/tax
//         if (req.getCategoryId() != null) {
//             e.setCategory(categoryRepo.findById(req.getCategoryId())
//                     .orElseThrow(() -> new EntityNotFoundException("Category not found: " + req.getCategoryId())));
//         }
//         if (req.getSubCategoryId() != null) {
//             e.setSubCategory(subCategoryRepo.findById(req.getSubCategoryId())
//                     .orElseThrow(() -> new EntityNotFoundException("SubCategory not found: " + req.getSubCategoryId())));
//         }
//         if (req.getIssueUnitId() != null) {
//             e.setIssueUnit(unitRepo.findById(req.getIssueUnitId())
//                     .orElseThrow(() -> new EntityNotFoundException("Issue unit not found: " + req.getIssueUnitId())));
//         }
//         if (req.getPurchaseUnitId() != null) {
//             e.setPurchaseUnit(unitRepo.findById(req.getPurchaseUnitId())
//                     .orElseThrow(() -> new EntityNotFoundException("Purchase unit not found: " + req.getPurchaseUnitId())));
//         }
//         if (req.getPriceCategoryId() != null) {
//             e.setPriceCategory(priceCategoryRepo.findById(req.getPriceCategoryId())
//                     .orElseThrow(() -> new EntityNotFoundException("Price category not found: " + req.getPriceCategoryId())));
//         }
//         if (req.getTaxId() != null) {
//             e.setTax(taxRepo.findById(req.getTaxId())
//                     .orElseThrow(() -> new EntityNotFoundException("Tax not found: " + req.getTaxId())));
//         }

//         // Many-to-many and BOM sets: use HashSet (no LinkedHashSet)
//         if (req.getBomItemIds() != null) {
//             Set<ProRawMaterials> boms = new HashSet<>(rawMaterialRepo.findAllById(req.getBomItemIds()));
//             e.setBomItems(boms);
//         } else {
//             e.setBomItems(new HashSet<>());
//         }

//         if (req.getProcessIds() != null) {
//             Set<ProProcess> procs = new HashSet<>(processRepo.findAllById(req.getProcessIds()));
//             e.setProcesses(procs);
//         } else {
//             e.setProcesses(new HashSet<>());
//         }

//         if (req.getToolIds() != null) {
//             Set<ProTools> tools = new HashSet<>(toolRepo.findAllById(req.getToolIds()));
//             e.setTools(tools);
//         } else {
//             e.setTools(new HashSet<>());
//         }

//         if (req.getToolStationIds() != null) {
//             Set<ProToolStation> stations = new HashSet<>(toolStationRepo.findAllById(req.getToolStationIds()));
//             e.setToolStations(stations);
//         } else {
//             e.setToolStations(new HashSet<>());
//         }

//         ProSemiFinishedGood saved = repo.save(e);
//         return toResponse(saved);
//     }

//     @Transactional(readOnly = true)
//     public Page<ProSemiFinishedGoodResponse> findAll(Pageable pageable) {
//         Tenant t = currentTenant();
//         return repo.findByTenantId(t.getId(), pageable).map(this::toResponse);
//     }

//     @Transactional(readOnly = true)
//     public ProSemiFinishedGoodResponse getById(Long id) {
//         Tenant t = currentTenant();
//         ProSemiFinishedGood e = repo.findByIdAndTenantId(id, t.getId())
//                 .orElseThrow(() -> new EntityNotFoundException("Semi finished good not found: " + id));
//         return toResponse(e);
//     }

//     public ProSemiFinishedGoodResponse update(Long id, ProSemiFinishedGoodRequest req) {
//         Tenant t = currentTenant();
//         ProSemiFinishedGood e = repo.findByIdAndTenantId(id, t.getId())
//                 .orElseThrow(() -> new EntityNotFoundException("Semi finished good not found: " + id));

//         // update simple fields if present
//         if (req.getName() != null) e.setName(req.getName());
//         if (req.getItemCode() != null) e.setItemCode(req.getItemCode());
//         if (req.getDescription() != null) e.setDescription(req.getDescription());
//         if (req.getInventoryType() != null) e.setInventoryType(req.getInventoryType());
//         if (req.getProduct() != null) e.setProduct(req.getProduct());
//         if (req.getService() != null) e.setService(req.getService());
//         if (req.getPurchase() != null) e.setPurchase(req.getPurchase());
//         if (req.getSales() != null) e.setSales(req.getSales());
//         if (req.getRoll() != null) e.setRoll(req.getRoll());
//         if (req.getScrapItem() != null) e.setScrapItem(req.getScrapItem());
//         if (req.getPurchaseToIssueRelation() != null) e.setPurchaseToIssueRelation(req.getPurchaseToIssueRelation());
//         if (req.getWastagePercent() != null) e.setWastagePercent(req.getWastagePercent());
//         if (req.getReorderLimit() != null) e.setReorderLimit(req.getReorderLimit());
//         if (req.getPurchasePrice() != null) e.setPurchasePrice(req.getPurchasePrice());
//         if (req.getSalesPrice() != null) e.setSalesPrice(req.getSalesPrice());
//         if (req.getTaxInclusive() != null) e.setTaxInclusive(req.getTaxInclusive());
//         if (req.getTaxRate() != null) e.setTaxRate(req.getTaxRate());
//         if (req.getImagePath() != null) e.setImagePath(req.getImagePath());

//         // relations
//         if (req.getCategoryId() != null) {
//             e.setCategory(categoryRepo.findById(req.getCategoryId())
//                     .orElseThrow(() -> new EntityNotFoundException("Category not found: " + req.getCategoryId())));
//         } else {
//             e.setCategory(null);
//         }
//         if (req.getSubCategoryId() != null) {
//             e.setSubCategory(subCategoryRepo.findById(req.getSubCategoryId())
//                     .orElseThrow(() -> new EntityNotFoundException("SubCategory not found: " + req.getSubCategoryId())));
//         } else {
//             e.setSubCategory(null);
//         }
//         if (req.getIssueUnitId() != null) {
//             e.setIssueUnit(unitRepo.findById(req.getIssueUnitId())
//                     .orElseThrow(() -> new EntityNotFoundException("Issue unit not found: " + req.getIssueUnitId())));
//         } else {
//             e.setIssueUnit(null);
//         }
//         if (req.getPurchaseUnitId() != null) {
//             e.setPurchaseUnit(unitRepo.findById(req.getPurchaseUnitId())
//                     .orElseThrow(() -> new EntityNotFoundException("Purchase unit not found: " + req.getPurchaseUnitId())));
//         } else {
//             e.setPurchaseUnit(null);
//         }
//         if (req.getPriceCategoryId() != null) {
//             e.setPriceCategory(priceCategoryRepo.findById(req.getPriceCategoryId())
//                     .orElseThrow(() -> new EntityNotFoundException("Price category not found: " + req.getPriceCategoryId())));
//         } else {
//             e.setPriceCategory(null);
//         }
//         if (req.getTaxId() != null) {
//             e.setTax(taxRepo.findById(req.getTaxId())
//                     .orElseThrow(() -> new EntityNotFoundException("Tax not found: " + req.getTaxId())));
//         } else {
//             e.setTax(null);
//         }

//         // update sets (replace when provided). Use HashSet (no LinkedHashSet)
//         if (req.getBomItemIds() != null) {
//             Set<ProRawMaterials> boms = new HashSet<>(rawMaterialRepo.findAllById(req.getBomItemIds()));
//             e.setBomItems(boms);
//         } else {
//             e.setBomItems(new HashSet<>());
//         }

//         if (req.getProcessIds() != null) {
//             Set<ProProcess> procs = new HashSet<>(processRepo.findAllById(req.getProcessIds()));
//             e.setProcesses(procs);
//         } else {
//             e.setProcesses(new HashSet<>());
//         }

//         if (req.getToolIds() != null) {
//             Set<ProTools> tools = new HashSet<>(toolRepo.findAllById(req.getToolIds()));
//             e.setTools(tools);
//         } else {
//             e.setTools(new HashSet<>());
//         }

//         if (req.getToolStationIds() != null) {
//             Set<ProToolStation> stations = new HashSet<>(toolStationRepo.findAllById(req.getToolStationIds()));
//             e.setToolStations(stations);
//         } else {
//             e.setToolStations(new HashSet<>());
//         }

//         ProSemiFinishedGood updated = repo.save(e);
//         return toResponse(updated);
//     }

//     public void delete(Long id) {
//         Tenant t = currentTenant();
//         ProSemiFinishedGood e = repo.findByIdAndTenantId(id, t.getId())
//                 .orElseThrow(() -> new EntityNotFoundException("Semi finished good not found: " + id));
//         repo.delete(e);
//     }

//     /* ---------- mapping helper ---------- */

//     private ProSemiFinishedGoodResponse toResponse(ProSemiFinishedGood e) {
//         ProSemiFinishedGoodResponse.ProSemiFinishedGoodResponseBuilder b = ProSemiFinishedGoodResponse.builder()
//                 .id(e.getId())
//                 .tenantId(e.getTenant() != null ? e.getTenant().getId() : null)
//                 .itemCode(e.getItemCode())
//                 .name(e.getName())
//                 .description(e.getDescription())
//                 .inventoryType(e.getInventoryType())
//                 .product(e.getProduct())
//                 .service(e.getService())
//                 .purchase(e.getPurchase())
//                 .sales(e.getSales())
//                 .roll(e.getRoll())
//                 .scrapItem(e.getScrapItem())
//                 .purchaseToIssueRelation(e.getPurchaseToIssueRelation())
//                 .wastagePercent(e.getWastagePercent())
//                 .reorderLimit(e.getReorderLimit())
//                 .purchasePrice(e.getPurchasePrice())
//                 .salesPrice(e.getSalesPrice())
//                 .taxInclusive(e.getTaxInclusive())
//                 .taxRate(e.getTaxRate())
//                 .imagePath(e.getImagePath())
//                 .createdAt(e.getCreatedAt())
//                 .updatedAt(e.getUpdatedAt());

//         if (e.getCategory() != null) {
//             b.categoryId(e.getCategory().getId()).categoryName(e.getCategory().getName());
//         }
//         if (e.getSubCategory() != null) {
//             b.subCategoryId(e.getSubCategory().getId()).subCategoryName(e.getSubCategory().getName());
//         }
//         if (e.getIssueUnit() != null) {
//             b.issueUnitId(e.getIssueUnit().getId()).issueUnitName(e.getIssueUnit().getName());
//         }
//         if (e.getPurchaseUnit() != null) {
//             b.purchaseUnitId(e.getPurchaseUnit().getId()).purchaseUnitName(e.getPurchaseUnit().getName());
//         }
//         if (e.getPriceCategory() != null) {
//             b.priceCategoryId(e.getPriceCategory().getId()).priceCategoryName(e.getPriceCategory().getName());
//         }
//         if (e.getTax() != null) {
//             b.taxId(e.getTax().getId()).taxName(e.getTax().getName());
//         }

//         b.bomItemIds(e.getBomItems() != null ? e.getBomItems().stream().map(ProRawMaterials::getId).collect(Collectors.toSet()) : Collections.emptySet());
//         b.processIds(e.getProcesses() != null ? e.getProcesses().stream().map(ProProcess::getId).collect(Collectors.toSet()) : Collections.emptySet());
//         b.toolIds(e.getTools() != null ? e.getTools().stream().map(ProTools::getId).collect(Collectors.toSet()) : Collections.emptySet());
//         b.toolStationIds(e.getToolStations() != null ? e.getToolStations().stream().map(ProToolStation::getId).collect(Collectors.toSet()) : Collections.emptySet());

//         return b.build();
//     }
// }

import com.example.multi_tanent.config.TenantContext;
import com.example.multi_tanent.production.dto.ProSemiFinishedGoodRequest;
import com.example.multi_tanent.production.dto.ProSemiFinishedGoodResponse;
import com.example.multi_tanent.production.entity.ProProcess;
import com.example.multi_tanent.production.entity.ProRawMaterials;
import com.example.multi_tanent.production.entity.ProSemiFinishedGood;
import com.example.multi_tanent.production.entity.ProToolStation;
import com.example.multi_tanent.production.entity.ProTools;
import com.example.multi_tanent.production.repository.*; // Added * for simplicity
import com.example.multi_tanent.spersusers.enitity.Tenant;
import com.example.multi_tanent.spersusers.repository.TenantRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional("tenantTx")
public class ProSemiFinishedGoodService {

    private final ProSemiFinishedGoodRepository repo;
    private final TenantRepository tenantRepo;

    private final ProCategoryRepository categoryRepo;
    private final ProSubCategoryRepository subCategoryRepo;
    private final ProUnitRepository unitRepo;
    private final ProPriceCategoryRepository priceCategoryRepo;
    private final ProTaxRepository taxRepo;
    private final ProRawMaterialsRepository rawMaterialRepo;
    private final ProProcessRepository processRepo;
    private final ProToolsRepository toolRepo;
    private final ProToolStationRepository toolStationRepo;

private Tenant currentTenant() {
String key = TenantContext.getTenantId();
return tenantRepo.findFirstByOrderByIdAsc()
.orElseThrow(() -> new IllegalStateException("Tenant not resolved for key: " + key));
 }

    public ProSemiFinishedGoodResponse create(ProSemiFinishedGoodRequest req) {
 Tenant t = currentTenant();

 ProSemiFinishedGood e = ProSemiFinishedGood.builder()
 .tenant(t)
 .itemCode(req.getItemCode())
 .name(req.getName())
 .description(req.getDescription())
 .inventoryType(req.getInventoryType())
 .product(Optional.ofNullable(req.getProduct()).orElse(Boolean.TRUE))
 .service(req.getService())
 .purchase(req.getPurchase())
 .sales(req.getSales())
 .roll(req.getRoll())
 .scrapItem(req.getScrapItem())
 .purchaseToIssueRelation(Optional.ofNullable(req.getPurchaseToIssueRelation()).orElse(BigDecimal.ONE))
 .wastagePercent(req.getWastagePercent())
 .reorderLimit(req.getReorderLimit())
 .purchasePrice(req.getPurchasePrice())
 .salesPrice(req.getSalesPrice())
.taxInclusive(Optional.ofNullable(req.getTaxInclusive()).orElse(Boolean.FALSE))
 .taxRate(req.getTaxRate())
 .imagePath(req.getImagePath())
 .build();


 if (req.getCategoryId() != null) {
 e.setCategory(categoryRepo.findById(req.getCategoryId())
 .orElseThrow(() -> new EntityNotFoundException("Category not found: " + req.getCategoryId())));
}
if (req.getSubCategoryId() != null) {
 e.setSubCategory(subCategoryRepo.findById(req.getSubCategoryId())
 .orElseThrow(() -> new EntityNotFoundException("SubCategory not found: " + req.getSubCategoryId())));
}
 if (req.getIssueUnitId() != null) {
 e.setIssueUnit(unitRepo.findById(req.getIssueUnitId())
 .orElseThrow(() -> new EntityNotFoundException("Issue unit not found: " + req.getIssueUnitId())));
 }
 if (req.getPurchaseUnitId() != null) {
 e.setPurchaseUnit(unitRepo.findById(req.getPurchaseUnitId())
.orElseThrow(() -> new EntityNotFoundException("Purchase unit not found: " + req.getPurchaseUnitId())));
}
 if (req.getPriceCategoryId() != null) {
 e.setPriceCategory(priceCategoryRepo.findById(req.getPriceCategoryId())
 .orElseThrow(() -> new EntityNotFoundException("Price category not found: " + req.getPriceCategoryId())));
 }
if (req.getTaxId() != null) {
e.setTax(taxRepo.findById(req.getTaxId())
 .orElseThrow(() -> new EntityNotFoundException("Tax not found: " + req.getTaxId())));
}

 // Many-to-many and BOM sets: use HashSet
 if (req.getBomItemIds() != null) {
 Set<ProRawMaterials> boms = new HashSet<>(rawMaterialRepo.findAllById(req.getBomItemIds()));
 e.setBomItems(boms);
 } else {
 e.setBomItems(new HashSet<>());
}

 if (req.getProcessIds() != null) {
 Set<ProProcess> procs = new HashSet<>(processRepo.findAllById(req.getProcessIds()));
 e.setProcesses(procs);
 } else {
 e.setProcesses(new HashSet<>());
 }

 if (req.getToolIds() != null) {
 Set<ProTools> tools = new HashSet<>(toolRepo.findAllById(req.getToolIds()));
 e.setTools(tools);
 } else {
 e.setTools(new HashSet<>());
 }

 if (req.getToolStationIds() != null) {
 Set<ProToolStation> stations = new HashSet<>(toolStationRepo.findAllById(req.getToolStationIds()));
 e.setToolStations(stations);
 } else {
 e.setToolStations(new HashSet<>());
 }

 ProSemiFinishedGood saved = repo.save(e);
 return toResponse(saved);
 }

    // =====================================================================
    // ✅ MODIFIED: findAll to include search parameter and logic
    // =====================================================================
  

    @Transactional(readOnly = true)
 public Page<ProSemiFinishedGoodResponse> findAll(String search, Pageable pageable) {
 Tenant t = currentTenant();
        
        // Check if the search term is empty or null
        if (search == null || search.trim().isEmpty()) {
            // Default behavior: find all items for the tenant
            return repo.findByTenantId(t.getId(), pageable).map(this::toResponse);
        } else {
            // Search behavior: use the custom repository method
            return repo.searchByTenantId(t.getId(), search.trim(), pageable).map(this::toResponse);
        }
 }
    // =====================================================================


    @Transactional(readOnly = true)
 public ProSemiFinishedGoodResponse getById(Long id) {
 Tenant t = currentTenant();
 ProSemiFinishedGood e = repo.findByIdAndTenantId(id, t.getId())
 .orElseThrow(() -> new EntityNotFoundException("Semi finished good not found: " + id));
 return toResponse(e);
 }

  
    public ProSemiFinishedGoodResponse update(Long id, ProSemiFinishedGoodRequest req) {
 Tenant t = currentTenant();
 ProSemiFinishedGood e = repo.findByIdAndTenantId(id, t.getId())
.orElseThrow(() -> new EntityNotFoundException("Semi finished good not found: " + id));


 if (req.getName() != null) e.setName(req.getName());
 if (req.getItemCode() != null) e.setItemCode(req.getItemCode());
 if (req.getDescription() != null) e.setDescription(req.getDescription());
 if (req.getInventoryType() != null) e.setInventoryType(req.getInventoryType());
 if (req.getProduct() != null) e.setProduct(req.getProduct());
 if (req.getService() != null) e.setService(req.getService());
 if (req.getPurchase() != null) e.setPurchase(req.getPurchase());
 if (req.getSales() != null) e.setSales(req.getSales());
 if (req.getRoll() != null) e.setRoll(req.getRoll());
 if (req.getScrapItem() != null) e.setScrapItem(req.getScrapItem());
 if (req.getPurchaseToIssueRelation() != null) e.setPurchaseToIssueRelation(req.getPurchaseToIssueRelation());
 if (req.getWastagePercent() != null) e.setWastagePercent(req.getWastagePercent());
 if (req.getReorderLimit() != null) e.setReorderLimit(req.getReorderLimit());
 if (req.getPurchasePrice() != null) e.setPurchasePrice(req.getPurchasePrice());
 if (req.getSalesPrice() != null) e.setSalesPrice(req.getSalesPrice());
 if (req.getTaxInclusive() != null) e.setTaxInclusive(req.getTaxInclusive());
 if (req.getTaxRate() != null) e.setTaxRate(req.getTaxRate());
 if (req.getImagePath() != null) e.setImagePath(req.getImagePath());


if (req.getCategoryId() != null) {
 e.setCategory(categoryRepo.findById(req.getCategoryId())
.orElseThrow(() -> new EntityNotFoundException("Category not found: " + req.getCategoryId())));
 } else {
 e.setCategory(null);
 }
 if (req.getSubCategoryId() != null) {
 e.setSubCategory(subCategoryRepo.findById(req.getSubCategoryId())
 .orElseThrow(() -> new EntityNotFoundException("SubCategory not found: " + req.getSubCategoryId())));
 } else {
 e.setSubCategory(null);
 }
 if (req.getIssueUnitId() != null) {
 e.setIssueUnit(unitRepo.findById(req.getIssueUnitId())
 .orElseThrow(() -> new EntityNotFoundException("Issue unit not found: " + req.getIssueUnitId())));
 } else {
 e.setIssueUnit(null);
 }
 if (req.getPurchaseUnitId() != null) {
 e.setPurchaseUnit(unitRepo.findById(req.getPurchaseUnitId())
 .orElseThrow(() -> new EntityNotFoundException("Purchase unit not found: " + req.getPurchaseUnitId())));
 } else {
 e.setPurchaseUnit(null);
 }
 if (req.getPriceCategoryId() != null) {
 e.setPriceCategory(priceCategoryRepo.findById(req.getPriceCategoryId())
 .orElseThrow(() -> new EntityNotFoundException("Price category not found: " + req.getPriceCategoryId())));
 } else {
 e.setPriceCategory(null);
 }
 if (req.getTaxId() != null) {
 e.setTax(taxRepo.findById(req.getTaxId())
 .orElseThrow(() -> new EntityNotFoundException("Tax not found: " + req.getTaxId())));
 } else {
 e.setTax(null);
 }

 // update sets (replace when provided). Use HashSet
 if (req.getBomItemIds() != null) {
 Set<ProRawMaterials> boms = new HashSet<>(rawMaterialRepo.findAllById(req.getBomItemIds()));
 e.setBomItems(boms);
 } else {
 e.setBomItems(new HashSet<>());
 }

 if (req.getProcessIds() != null) {
 Set<ProProcess> procs = new HashSet<>(processRepo.findAllById(req.getProcessIds()));
 e.setProcesses(procs);
 } else {
 e.setProcesses(new HashSet<>());
}

 if (req.getToolIds() != null) {
 Set<ProTools> tools = new HashSet<>(toolRepo.findAllById(req.getToolIds()));
 e.setTools(tools);
 } else {
 e.setTools(new HashSet<>());
 }

 if (req.getToolStationIds() != null) {
 Set<ProToolStation> stations = new HashSet<>(toolStationRepo.findAllById(req.getToolStationIds()));
e.setToolStations(stations);
 } else {
 e.setToolStations(new HashSet<>());
 }

 ProSemiFinishedGood updated = repo.save(e);
 return toResponse(updated);
}

   

    public void delete(Long id) {
 Tenant t = currentTenant();
 ProSemiFinishedGood e = repo.findByIdAndTenantId(id, t.getId())
 .orElseThrow(() -> new EntityNotFoundException("Semi finished good not found: " + id));
 repo.delete(e);
}

    private ProSemiFinishedGoodResponse toResponse(ProSemiFinishedGood e) {
 ProSemiFinishedGoodResponse.ProSemiFinishedGoodResponseBuilder b = ProSemiFinishedGoodResponse.builder()
 .id(e.getId())
 .tenantId(e.getTenant() != null ? e.getTenant().getId() : null)
 .itemCode(e.getItemCode())
 .name(e.getName())
 .description(e.getDescription())
.inventoryType(e.getInventoryType())
 .product(e.getProduct())
 .service(e.getService())
 .purchase(e.getPurchase())
 .sales(e.getSales())
 .roll(e.getRoll())
 .scrapItem(e.getScrapItem())
 .purchaseToIssueRelation(e.getPurchaseToIssueRelation())
 .wastagePercent(e.getWastagePercent())
 .reorderLimit(e.getReorderLimit())
 .purchasePrice(e.getPurchasePrice())
 .salesPrice(e.getSalesPrice())
 .taxInclusive(e.getTaxInclusive())
 .taxRate(e.getTaxRate())
 .imagePath(e.getImagePath())
 .createdAt(e.getCreatedAt())
.updatedAt(e.getUpdatedAt());

 if (e.getCategory() != null) {
 b.categoryId(e.getCategory().getId()).categoryName(e.getCategory().getName());
 }
 if (e.getSubCategory() != null) {
 b.subCategoryId(e.getSubCategory().getId()).subCategoryName(e.getSubCategory().getName());
}
 if (e.getIssueUnit() != null) {
 b.issueUnitId(e.getIssueUnit().getId()).issueUnitName(e.getIssueUnit().getName());
 }
 if (e.getPurchaseUnit() != null) {
 b.purchaseUnitId(e.getPurchaseUnit().getId()).purchaseUnitName(e.getPurchaseUnit().getName());
 }
 if (e.getPriceCategory() != null) {
 b.priceCategoryId(e.getPriceCategory().getId()).priceCategoryName(e.getPriceCategory().getName());
 }
 if (e.getTax() != null) {
 b.taxId(e.getTax().getId()).taxName(e.getTax().getName());
}

 b.bomItemIds(e.getBomItems() != null ? e.getBomItems().stream().map(ProRawMaterials::getId).collect(Collectors.toSet()) : Collections.emptySet());
 b.processIds(e.getProcesses() != null ? e.getProcesses().stream().map(ProProcess::getId).collect(Collectors.toSet()) : Collections.emptySet());
 b.toolIds(e.getTools() != null ? e.getTools().stream().map(ProTools::getId).collect(Collectors.toSet()) : Collections.emptySet());
 b.toolStationIds(e.getToolStations() != null ? e.getToolStations().stream().map(ProToolStation::getId).collect(Collectors.toSet()) : Collections.emptySet());

return b.build();
 }
}