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

import com.example.multi_tanent.purchases.entity.PurchaseBill;
import com.example.multi_tanent.purchases.service.PurchaseBillService;


@RestController
@RequestMapping("/api/purchases/bills")
@CrossOrigin(origins = "*")
@PreAuthorize("hasAnyRole('SUPER_ADMIN','PURCHASE_ADMIN')")
public class PurchaseBillController {

    private final PurchaseBillService service;

    public PurchaseBillController(PurchaseBillService service) {
        this.service = service;
    }

    @GetMapping
    public List<PurchaseBill> getAllBills() {
        return service.getAllBills();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseBill> getBillById(@PathVariable Long id) {
        PurchaseBill bill = service.getBillById(id);
        return bill != null ? ResponseEntity.ok(bill) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public PurchaseBill createBill(@RequestBody PurchaseBill bill, @RequestParam(required = false) Long purchaseOrderId) {
        return service.createBill(bill, purchaseOrderId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PurchaseBill> updateBill(@PathVariable Long id, @RequestBody PurchaseBill updated) {
        PurchaseBill saved = service.updateBill(id, updated);
        return saved != null ? ResponseEntity.ok(saved) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public void deleteBill(@PathVariable Long id) {
        service.deleteBill(id);
    }
}
