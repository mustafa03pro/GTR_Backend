package com.example.multi_tanent.production.entity;




import java.math.BigDecimal;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "pro_bom_item_material")
public class ProBomItemMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Parent BOM row */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bom_item_id", nullable = false)
    private ProBomItem bomItem;

    /** Actual raw material / masterbatch reference chosen in the UI */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "raw_material_id", nullable = false)
    private ProRawMaterials rawMaterial;

    /** Unit for the quantity (Issue unit — e.g., KGS) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id")
    private ProUnit unit;

    /** Quantity in issue unit (required in UI) */
    @NotNull
    @DecimalMin(value = "0.000001", inclusive = true)
    @Column(name = "quantity", precision = 19, scale = 6, nullable = false)
    private BigDecimal quantity;

    /** Optional per-item note */
    @Column(name = "notes", length = 1000)
    private String notes;

    // Getters / setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public ProBomItem getBomItem() { return bomItem; }
    public void setBomItem(ProBomItem bomItem) { this.bomItem = bomItem; }

    public ProRawMaterials getRawMaterial() { return rawMaterial; }
    public void setRawMaterial(ProRawMaterials rawMaterial) { this.rawMaterial = rawMaterial; }

    public ProUnit getUnit() { return unit; }
    public void setUnit(ProUnit unit) { this.unit = unit; }

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProBomItemMaterial)) return false;
        ProBomItemMaterial that = (ProBomItemMaterial) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

