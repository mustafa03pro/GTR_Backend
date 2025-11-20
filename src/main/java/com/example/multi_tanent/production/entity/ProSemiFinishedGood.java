package com.example.multi_tanent.production.entity;

import com.example.multi_tanent.spersusers.enitity.Tenant;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.*;

@Entity
@Table(name = "pro_semi_finished_goods",
       indexes = {
           @Index(name = "idx_sfg_tenant", columnList = "tenant_id"),
           @Index(name = "idx_sfg_item_code", columnList = "item_code")
       })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProSemiFinishedGood {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Tenant link
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false, foreignKey = @ForeignKey(name = "fk_sfg_tenant"))
    private Tenant tenant;

    @Column(name = "item_code", length = 100, unique = true)
    private String itemCode;

    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    // Inventory / Item type flags
    @Column(name = "inventory_type")
    private String inventoryType; // e.g., "Semi Finished Good" or ref id to inventory type entity

    @Column(name = "is_product")
    private Boolean product = Boolean.TRUE;

    @Column(name = "is_service")
    private Boolean service = Boolean.FALSE;

    @Column(name = "is_purchase")
    private Boolean purchase = Boolean.FALSE;

    @Column(name = "is_sales")
    private Boolean sales = Boolean.TRUE;

    @Column(name = "is_roll")
    private Boolean roll = Boolean.FALSE;

    @Column(name = "is_scrap_item")
    private Boolean scrapItem = Boolean.FALSE;

    // Category / subcategory relationships (optional)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", foreignKey = @ForeignKey(name = "fk_sfg_category"))
    private ProCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_category_id", foreignKey = @ForeignKey(name = "fk_sfg_subcategory"))
    private ProSubCategory subCategory;

    // Units
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issue_unit_id", foreignKey = @ForeignKey(name = "fk_sfg_issue_unit"))
    private ProUnit issueUnit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_unit_id", foreignKey = @ForeignKey(name = "fk_sfg_purchase_unit"))
    private ProUnit purchaseUnit;

    @Column(name = "purchase_to_issue_relation", precision = 18, scale = 6)
    private BigDecimal purchaseToIssueRelation = BigDecimal.ONE;

    @Column(name = "wastage_percent", precision = 7, scale = 4)
    private BigDecimal wastagePercent;

    @Column(name = "reorder_level")
    private Integer reorderLimit;

    // Pricing & tax
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "price_category_id", foreignKey = @ForeignKey(name = "fk_sfg_price_category"))
    private ProPriceCategory priceCategory;

    @Column(name = "purchase_price", precision = 19, scale = 4)
    private BigDecimal purchasePrice;

    @Column(name = "sales_price", precision = 19, scale = 4)
    private BigDecimal salesPrice;

    @Column(name = "tax_inclusive")
    private Boolean taxInclusive = Boolean.FALSE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tax_id", foreignKey = @ForeignKey(name = "fk_sfg_tax"))
    private ProTax tax;

    @Column(name = "tax_rate", precision = 7, scale = 4)
    private BigDecimal taxRate;

    @Column(name = "image_path", length = 1024)
    private String imagePath;

    // BOM Items - one-to-many
    @ManyToMany
    @JoinTable(name = "pro_sfg_bom_items",
            joinColumns = @JoinColumn(name = "sfg_id", foreignKey = @ForeignKey(name = "fk_sfg_bom_sfg")),
            inverseJoinColumns = @JoinColumn(name = "raw_material_id", foreignKey = @ForeignKey(name = "fk_sfg_bom_raw_material")))
    @Builder.Default
    private Set<ProRawMaterials> bomItems = new LinkedHashSet<>();

    // Processes (many-to-many)
    @ManyToMany
    @JoinTable(name = "pro_sfg_processes",
            joinColumns = @JoinColumn(name = "sfg_id"),
            inverseJoinColumns = @JoinColumn(name = "process_id"))
    @Builder.Default
    private Set<ProProcess> processes = new LinkedHashSet<>();

    // Tools & tool stations
    @ManyToMany
    @JoinTable(name = "pro_sfg_tools",
            joinColumns = @JoinColumn(name = "sfg_id"),
            inverseJoinColumns = @JoinColumn(name = "tool_id"))
    @Builder.Default
    private Set<ProTools> tools = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(name = "pro_sfg_tool_stations",
            joinColumns = @JoinColumn(name = "sfg_id"),
            inverseJoinColumns = @JoinColumn(name = "tool_station_id"))
    @Builder.Default
    private Set<ProToolStation> toolStations = new LinkedHashSet<>();

    // Audit
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @PrePersist
    void prePersist() {
        createdAt = OffsetDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = OffsetDateTime.now();
    }
}
