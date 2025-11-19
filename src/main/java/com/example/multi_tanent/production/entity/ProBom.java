package com.example.multi_tanent.production.entity;

import com.example.multi_tanent.spersusers.enitity.Tenant;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "pro_bom")
public class ProBom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Required: product shown at top of Add BOM form */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "semi_finished_good_id", nullable = false)
    private ProSemiFinishedGood product;

    /** BOM name (prefilled in UI) - required */
    @NotBlank
    @Column(name = "bom_name", length = 255, nullable = false)
    private String bomName;

    /** Lock/Unlock dropdown (UI) */
    @NotNull
    @Column(name = "locked", nullable = false)
    private Boolean locked = Boolean.FALSE;

    /** Optional: Copy from an existing BOM (self reference) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "copy_from_bom_id")
    private ProBom copyFromBom;

    /**
     * Tenant relation — replace with tenantId:Long if your project doesn't have ProTenant.
     * @see comment below if ProTenant isn't present in your codebase.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id")
    private Tenant tenant;
    // If you don't have ProTenant in your project, replace the above with:
    // @Column(name="tenant_id") private Long tenantId;

    /** Created meta */
    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /** BOM rows */
    @OneToMany(mappedBy = "bom", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("lineNumber ASC")
    private List<ProBomItem> items = new ArrayList<>();

    // Convenience helpers
    public void addItem(ProBomItem item) {
        item.setBom(this);
        items.add(item);
    }

    public void removeItem(ProBomItem item) {
        items.remove(item);
        item.setBom(null);
    }

    // Getters / setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public ProSemiFinishedGood getProduct() { return product; }
    public void setProduct(ProSemiFinishedGood product) { this.product = product; }

    public String getBomName() { return bomName; }
    public void setBomName(String bomName) { this.bomName = bomName; }

    public Boolean getLocked() { return locked; }
    public void setLocked(Boolean locked) { this.locked = locked; }

    public ProBom getCopyFromBom() { return copyFromBom; }
    public void setCopyFromBom(ProBom copyFromBom) { this.copyFromBom = copyFromBom; }

    public Tenant getTenant() { return tenant; }
    public void setTenant(Tenant tenant) { this.tenant = tenant; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<ProBomItem> getItems() { return items; }
    public void setItems(List<ProBomItem> items) {
        this.items.clear();
        if (items != null) {
            items.forEach(this::addItem);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProBom)) return false;
        ProBom proBom = (ProBom) o;
        return id != null && id.equals(proBom.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
