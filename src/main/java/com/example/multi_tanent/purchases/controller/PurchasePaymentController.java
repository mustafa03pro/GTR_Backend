package com.example.multi_tanent.purchases.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.multi_tanent.purchases.service.PurchasePaymentService;
import com.example.multi_tanent.purchases.entity.PurchasePayment;


@RestController
@RequestMapping("/api/purchases/payments")
@CrossOrigin(origins = "*")
@PreAuthorize("hasAnyRole('SUPER_ADMIN','PURCHASE_ADMIN')")
public class PurchasePaymentController {

    private final PurchasePaymentService service;

    public PurchasePaymentController(PurchasePaymentService service) {
        this.service = service;
    }

    @GetMapping
    public List<PurchasePayment> getAllPayments() {
        return service.getAllPayments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchasePayment> getPaymentById(@PathVariable Long id) {
        PurchasePayment payment = service.getPaymentById(id);
        return payment != null ? ResponseEntity.ok(payment) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public PurchasePayment createPayment(@RequestBody PurchasePayment payment, @RequestParam(required = false) Long billId) {
        return service.createPayment(payment, billId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PurchasePayment> updatePayment(@PathVariable Long id, @RequestBody PurchasePayment updated) {
        PurchasePayment saved = service.updatePayment(id, updated);
        return saved != null ? ResponseEntity.ok(saved) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public void deletePayment(@PathVariable Long id) {
        service.deletePayment(id);
    }
}
