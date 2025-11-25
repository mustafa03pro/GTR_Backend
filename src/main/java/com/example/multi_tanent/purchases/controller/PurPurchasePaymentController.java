package com.example.multi_tanent.purchases.controller;

// package com.example.multi_tanent.purchases.controller;

import com.example.multi_tanent.purchases.dto.*;
import com.example.multi_tanent.purchases.service.PurPurchasePaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/purchases/payments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PurPurchasePaymentController {

    private final PurPurchasePaymentService service;

    @PostMapping
    public ResponseEntity<PurPurchasePaymentResponse> create(@Valid @RequestBody PurPurchasePaymentRequest req) {
        PurPurchasePaymentResponse resp = service.create(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @GetMapping
    public Page<PurPurchasePaymentResponse> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort) {
        Sort s = Sort.by(Sort.Direction.DESC, "createdAt");
        try {
            String[] sp = sort.split(",");
            s = Sort.by(Sort.Direction.fromString(sp[1]), sp[0]);
        } catch (Exception ignored) {
        }
        Pageable p = PageRequest.of(page, size, s);
        return service.list(p);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurPurchasePaymentResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PurPurchasePaymentResponse> update(@PathVariable Long id,
            @Valid @RequestBody PurPurchasePaymentRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
