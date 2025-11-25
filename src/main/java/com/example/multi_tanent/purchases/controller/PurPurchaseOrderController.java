package com.example.multi_tanent.purchases.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.example.multi_tanent.purchases.dto.PurPurchaseOrderRequest;
import com.example.multi_tanent.purchases.dto.PurPurchaseOrderResponse;
import com.example.multi_tanent.purchases.service.PurPurchaseOrderService;

@RestController
@RequestMapping("/api/purchase/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PurPurchaseOrderController {

    private final PurPurchaseOrderService service;

    @PostMapping
    public ResponseEntity<PurPurchaseOrderResponse> create(@Valid @RequestBody PurPurchaseOrderRequest req) {
        PurPurchaseOrderResponse resp = service.create(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @GetMapping
    public ResponseEntity<Page<PurPurchaseOrderResponse>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort // e.g. "createdAt,desc"
    ) {
        Sort s = Sort.by(Sort.Direction.DESC, "createdAt");
        try {
            String[] sp = sort.split(",");
            if (sp.length == 2) {
                s = Sort.by(Sort.Direction.fromString(sp[1]), sp[0]);
            }
        } catch (Exception ignored) {
        }
        Pageable p = PageRequest.of(page, size, s);
        return ResponseEntity.ok(service.list(p));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurPurchaseOrderResponse> getById(@PathVariable Long id) {
        PurPurchaseOrderResponse resp = service.getById(id);
        return ResponseEntity.ok(resp);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PurPurchaseOrderResponse> update(@PathVariable Long id,
            @Valid @RequestBody PurPurchaseOrderRequest req) {
        PurPurchaseOrderResponse resp = service.update(id, req);
        return ResponseEntity.ok(resp);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
