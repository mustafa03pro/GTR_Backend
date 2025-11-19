package com.example.multi_tanent.production.dto;
import lombok.*;


import jakarta.validation.constraints.NotNull;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProBomItemRequest {
    @NotNull
    private Integer lineNumber;

    private Long processId;
    private Long categoryId;
    private Long subCategoryId;

    private List<ProBomItemMaterialRequest> materials;
}
