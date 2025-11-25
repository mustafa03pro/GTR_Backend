package com.example.multi_tanent.purchases.service;

// package com.example.multi_tanent.purchases.service;

import com.example.multi_tanent.config.TenantContext;
import com.example.multi_tanent.purchases.dto.*;
import com.example.multi_tanent.purchases.entity.*;
import com.example.multi_tanent.purchases.repository.*;
import com.example.multi_tanent.spersusers.enitity.BaseCustomer;
import com.example.multi_tanent.spersusers.enitity.Tenant;
import com.example.multi_tanent.spersusers.repository.PartyRepository;
import com.example.multi_tanent.spersusers.repository.TenantRepository;
import com.example.multi_tanent.purchases.entity.PurPurchaseInvoice;
import com.example.multi_tanent.purchases.repository.PurPurchaseInvoiceRepository; // for invoice lookup

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional("tenantTx")
public class PurPurchasePaymentService {

    private final PurPurchasePaymentRepository repo;
    private final PurPurchasePaymentAllocationRepository allocRepo;
    private final PurPurchasePaymentAttachmentRepository attachmentRepo;
    private final TenantRepository tenantRepo;
    private final PartyRepository partyRepo; // supplier repo
    private final PurPurchaseInvoiceRepository invoiceRepo;

    private Tenant currentTenant() {
        String key = TenantContext.getTenantId();
        return tenantRepo.findFirstByOrderByIdAsc()
                .orElseThrow(() -> new IllegalStateException("Tenant not resolved for key: " + key));
    }

    public PurPurchasePaymentResponse create(PurPurchasePaymentRequest req) {
        Tenant t = currentTenant();

        PurPurchasePayment p = new PurPurchasePayment();
        p.setSupplier(req.getSupplierId() != null ? partyRepo.findById(req.getSupplierId())
                .orElseThrow(() -> new EntityNotFoundException("Supplier not found: " + req.getSupplierId())) : null);
        p.setAmount(req.getAmount());
        p.setPayFullAmount(Optional.ofNullable(req.getPayFullAmount()).orElse(Boolean.FALSE));
        p.setTaxDeducted(Optional.ofNullable(req.getTaxDeducted()).orElse(Boolean.FALSE));
        p.setTdsAmount(Optional.ofNullable(req.getTdsAmount()).orElse(BigDecimal.ZERO));
        p.setTdsSection(req.getTdsSection());
        p.setPaymentDate(req.getPaymentDate());
        p.setPaymentMode(req.getPaymentMode());
        p.setPaidThrough(req.getPaidThrough());
        p.setReference(req.getReference());
        p.setChequeNumber(req.getChequeNumber());
        p.setNotes(req.getNotes());
        p.setCreatedBy(req.getCreatedBy());
        p.setCreatedAt(LocalDateTime.now());
        p.setTenant(t);

        // allocations
        p.setAllocations(new ArrayList<>());
        if (req.getAllocations() != null) {
            for (PurPurchasePaymentAllocationRequest ar : req.getAllocations()) {
                PurPurchaseInvoice inv = invoiceRepo.findById(ar.getInvoiceId())
                        .orElseThrow(() -> new EntityNotFoundException("Invoice not found: " + ar.getInvoiceId()));
                PurPurchasePaymentAllocation a = new PurPurchasePaymentAllocation();
                a.setPurchaseInvoice(inv);
                a.setAllocatedAmount(ar.getAllocatedAmount());
                a.setAllocationNote(ar.getAllocationNote());
                a.setPurchasePayment(p);
                p.getAllocations().add(a);
            }
        }

        // attachments
        p.setAttachments(new ArrayList<>());
        if (req.getAttachments() != null) {
            for (PurPurchasePaymentAttachmentRequest ar : req.getAttachments()) {
                PurPurchasePaymentAttachment att = new PurPurchasePaymentAttachment();
                att.setFileName(ar.getFileName());
                att.setFilePath(ar.getFilePath());
                att.setUploadedBy(ar.getUploadedBy());
                att.setUploadedAt(ar.getUploadedAt());
                att.setPurchasePayment(p);
                p.getAttachments().add(att);
            }
        }

        // compute simple derived fields (sum of allocations)
        recomputePaymentFields(p);

        PurPurchasePayment saved = repo.save(p);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public Page<PurPurchasePaymentResponse> list(Pageable pageable) {
        Tenant t = currentTenant();
        return repo.findByTenantId(t.getId(), pageable).map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public PurPurchasePaymentResponse getById(Long id) {
        Tenant t = currentTenant();
        PurPurchasePayment p = repo.findByIdAndTenantId(id, t.getId())
                .orElseThrow(() -> new EntityNotFoundException("Purchase payment not found: " + id));
        return toResponse(p);
    }

    public PurPurchasePaymentResponse update(Long id, PurPurchasePaymentRequest req) {
        Tenant t = currentTenant();
        PurPurchasePayment p = repo.findByIdAndTenantId(id, t.getId())
                .orElseThrow(() -> new EntityNotFoundException("Purchase payment not found: " + id));

        if (req.getSupplierId() != null) {
            p.setSupplier(partyRepo.findById(req.getSupplierId())
                    .orElseThrow(() -> new EntityNotFoundException("Supplier not found: " + req.getSupplierId())));
        } else {
            p.setSupplier(null);
        }

        if (req.getAmount() != null)
            p.setAmount(req.getAmount());
        if (req.getPayFullAmount() != null)
            p.setPayFullAmount(req.getPayFullAmount());
        if (req.getTaxDeducted() != null)
            p.setTaxDeducted(req.getTaxDeducted());
        if (req.getTdsAmount() != null)
            p.setTdsAmount(req.getTdsAmount());
        if (req.getTdsSection() != null)
            p.setTdsSection(req.getTdsSection());
        if (req.getPaymentDate() != null)
            p.setPaymentDate(req.getPaymentDate());
        if (req.getPaymentMode() != null)
            p.setPaymentMode(req.getPaymentMode());
        if (req.getPaidThrough() != null)
            p.setPaidThrough(req.getPaidThrough());
        if (req.getReference() != null)
            p.setReference(req.getReference());
        if (req.getChequeNumber() != null)
            p.setChequeNumber(req.getChequeNumber());
        if (req.getNotes() != null)
            p.setNotes(req.getNotes());
        if (req.getCreatedBy() != null)
            p.setCreatedBy(req.getCreatedBy());

        // Replace allocations if provided
        if (req.getAllocations() != null) {
            // clear existing allocations (or keep behavior of merge based on your needs)
            p.getAllocations().clear();
            for (PurPurchasePaymentAllocationRequest ar : req.getAllocations()) {
                PurPurchaseInvoice inv = invoiceRepo.findById(ar.getInvoiceId())
                        .orElseThrow(() -> new EntityNotFoundException("Invoice not found: " + ar.getInvoiceId()));
                PurPurchasePaymentAllocation a = new PurPurchasePaymentAllocation();
                a.setPurchaseInvoice(inv);
                a.setAllocatedAmount(ar.getAllocatedAmount());
                a.setAllocationNote(ar.getAllocationNote());
                a.setPurchasePayment(p);
                p.getAllocations().add(a);
            }
        }

        // Replace attachments if provided
        if (req.getAttachments() != null) {
            p.getAttachments().clear();
            for (PurPurchasePaymentAttachmentRequest ar : req.getAttachments()) {
                PurPurchasePaymentAttachment att = new PurPurchasePaymentAttachment();
                att.setFileName(ar.getFileName());
                att.setFilePath(ar.getFilePath());
                att.setUploadedBy(ar.getUploadedBy());
                att.setUploadedAt(ar.getUploadedAt());
                att.setPurchasePayment(p);
                p.getAttachments().add(att);
            }
        }

        recomputePaymentFields(p);

        PurPurchasePayment updated = repo.save(p);
        return toResponse(updated);
    }

    public void delete(Long id) {
        Tenant t = currentTenant();
        PurPurchasePayment p = repo.findByIdAndTenantId(id, t.getId())
                .orElseThrow(() -> new EntityNotFoundException("Purchase payment not found: " + id));
        repo.delete(p);
    }

    /* ---------------- helpers ---------------- */

    private void recomputePaymentFields(PurPurchasePayment p) {
        // Basic derived values: amountPaid = sum allocated amounts (or min(amount,
        // sum))
        BigDecimal sumAllocated = Optional.ofNullable(p.getAllocations())
                .orElse(Collections.emptyList())
                .stream()
                .map(a -> Optional.ofNullable(a.getAllocatedAmount()).orElse(BigDecimal.ZERO))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        p.setAmountPaid(sumAllocated);
        // amountUsedForPayments equals allocated total (business logic may differ)
        p.setAmountUsedForPayments(sumAllocated);
        // simple placeholders
        p.setAmountRefunded(Optional.ofNullable(p.getAmountRefunded()).orElse(BigDecimal.ZERO));
        BigDecimal inExcess = Optional.ofNullable(p.getAmount()).orElse(BigDecimal.ZERO).subtract(sumAllocated);
        p.setAmountInExcess(inExcess.compareTo(BigDecimal.ZERO) > 0 ? inExcess : BigDecimal.ZERO);
    }

    private PurPurchasePaymentResponse toResponse(PurPurchasePayment p) {
        PurPurchasePaymentResponse.PurPurchasePaymentResponseBuilder rb = PurPurchasePaymentResponse.builder()
                .id(p.getId())
                .supplierId(p.getSupplier() != null ? p.getSupplier().getId() : null)
                .supplierName(p.getSupplier() != null ? p.getSupplier().getCompanyName() : null)
                .amount(p.getAmount())
                .payFullAmount(p.getPayFullAmount())
                .taxDeducted(p.getTaxDeducted())
                .tdsAmount(p.getTdsAmount())
                .tdsSection(p.getTdsSection())
                .paymentDate(p.getPaymentDate())
                .paymentMode(p.getPaymentMode())
                .paidThrough(p.getPaidThrough())
                .reference(p.getReference())
                .chequeNumber(p.getChequeNumber())
                .amountPaid(p.getAmountPaid())
                .amountUsedForPayments(p.getAmountUsedForPayments())
                .amountRefunded(p.getAmountRefunded())
                .amountInExcess(p.getAmountInExcess())
                .notes(p.getNotes())
                .createdBy(p.getCreatedBy())
                .createdAt(p.getCreatedAt())
                .tenantId(p.getTenant() != null ? p.getTenant().getId() : null);

        List<PurPurchasePaymentAllocationResponse> allocs = Optional.ofNullable(p.getAllocations())
                .orElse(Collections.emptyList())
                .stream()
                .map(a -> PurPurchasePaymentAllocationResponse.builder()
                        .id(a.getId())
                        .invoiceId(a.getPurchaseInvoice() != null ? a.getPurchaseInvoice().getId() : null)
                        .invoiceNumber(a.getPurchaseInvoice() != null ? a.getPurchaseInvoice().getOrderNumber() : null)
                        .allocatedAmount(a.getAllocatedAmount())
                        .allocationNote(a.getAllocationNote())
                        .build())
                .collect(Collectors.toList());
        rb.allocations(allocs);

        List<PurPurchasePaymentAttachmentResponse> atts = Optional.ofNullable(p.getAttachments())
                .orElse(Collections.emptyList())
                .stream()
                .map(a -> PurPurchasePaymentAttachmentResponse.builder()
                        .id(a.getId())
                        .fileName(a.getFileName())
                        .filePath(a.getFilePath())
                        .uploadedBy(a.getUploadedBy())
                        .uploadedAt(a.getUploadedAt())
                        .build())
                .collect(Collectors.toList());
        rb.attachments(atts);

        return rb.build();
    }
}
