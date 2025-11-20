package com.example.multi_tanent.production.dto;
import lombok.*;


import java.util.List;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProBomItemResponse {
    private Long id;
    private Integer lineNumber;

    private Long processId;
    private String processName;

    private Long categoryId;
    private String categoryName;

    private Long subCategoryId;
    private String subCategoryName;

    private List<ProBomItemMaterialResponse> materials;
}
