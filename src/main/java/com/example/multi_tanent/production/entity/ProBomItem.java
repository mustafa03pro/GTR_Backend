package com.example.multi_tanent.production.entity;




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
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "pro_bom_item",
       uniqueConstraints = @UniqueConstraint(columnNames = {"bom_id", "line_number"}))
public class ProBomItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Sr.no displayed in UI */
    @NotNull
    @Column(name = "line_number", nullable = false)
    private Integer lineNumber;

    /** owning BOM */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bom_id", nullable = false)
    private ProBom bom;

    /** Process selected (MIXING/EXTRUSION/PRINTING) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "process_id")
    private ProProcess process;

    /** Category/Subcategory filters used to drive the item selector in UI (optional) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private ProCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_category_id")
    private ProSubCategory subCategory;

    /**
     * Materials selected in this row. UI shows multiple item "chips" inside a row,
     * so we store each selected item as a separate ProBomItemMaterial entry.
     */
    @OneToMany(mappedBy = "bomItem", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id ASC")
    private List<ProBomItemMaterial> materials = new ArrayList<>();

    // helpers
    public void addMaterial(ProBomItemMaterial m) {
        m.setBomItem(this);
        materials.add(m);
    }

    public void removeMaterial(ProBomItemMaterial m) {
        materials.remove(m);
        m.setBomItem(null);
    }

    // Getters / setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getLineNumber() { return lineNumber; }
    public void setLineNumber(Integer lineNumber) { this.lineNumber = lineNumber; }

    public ProBom getBom() { return bom; }
    public void setBom(ProBom bom) { this.bom = bom; }

    public ProProcess getProcess() { return process; }
    public void setProcess(ProProcess process) { this.process = process; }

    public ProCategory getCategory() { return category; }
    public void setCategory(ProCategory category) { this.category = category; }

    public ProSubCategory getSubCategory() { return subCategory; }
    public void setSubCategory(ProSubCategory subCategory) { this.subCategory = subCategory; }

    public List<ProBomItemMaterial> getMaterials() { return materials; }
    public void setMaterials(List<ProBomItemMaterial> materials) {
        this.materials.clear();
        if (materials != null) materials.forEach(this::addMaterial);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProBomItem)) return false;
        ProBomItem that = (ProBomItem) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

