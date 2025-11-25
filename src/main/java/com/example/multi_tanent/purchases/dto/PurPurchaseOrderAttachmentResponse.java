package com.example.multi_tanent.purchases.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PurPurchaseOrderAttachmentResponse {
    private Long id;
    private String fileName;
    private String filePath;
    private String uploadedBy;
    private LocalDateTime uploadedAt;
}
