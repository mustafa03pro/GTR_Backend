package com.example.multi_tanent.production.dto;

// package com.example.multi_tanent.production.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;


@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProBomRequest {
    @NotNull
    private Long productId;

    @NotBlank
    private String bomName;

    private Boolean locked = Boolean.FALSE;

    // Optional copy-from existing BOM
    private Long copyFromBomId;

    // Optional meta
    private String createdBy;

    // BOM rows
    private List<ProBomItemRequest> items;
}


