package com.example.multi_tanent.purchases.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.multi_tanent.purchases.entity.PurPurchaseOrder;
import com.example.multi_tanent.purchases.service.PurPurchaseOrderService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/purchases/purchase-orders")
@CrossOrigin(origins = "*")
@PreAuthorize("hasAnyRole('SUPER_ADMIN','PURCHASE_ADMIN')")
public class PurPurchaseOrderController {
    private final PurPurchaseOrderService service;

    public PurPurchaseOrderController(PurPurchaseOrderService service) {
        this.service = service;
    }

    @GetMapping
    public List<PurPurchaseOrder> getAll() { return service.getAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<PurPurchaseOrder> getById(@PathVariable Long id) {
        PurPurchaseOrder po = service.getById(id);
        return po != null ? ResponseEntity.ok(po) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public PurPurchaseOrder create(@RequestBody PurPurchaseOrder po) {
        return service.save(po);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PurPurchaseOrder> update(@PathVariable Long id, @RequestBody PurPurchaseOrder updated) {
        PurPurchaseOrder po = service.getById(id);
        if (po == null) return ResponseEntity.notFound().build();
        updated.setId(id);
        return ResponseEntity.ok(service.save(updated));
    }

    @PostMapping("/{id}/attachment")
    public ResponseEntity<String> uploadFile(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws IOException {
        PurPurchaseOrder po = service.getById(id);
        if (po == null) return ResponseEntity.notFound().build();
        po.setAttachment(file.getBytes());
        service.save(po);
        return ResponseEntity.ok("File uploaded");
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { service.delete(id); }
}
