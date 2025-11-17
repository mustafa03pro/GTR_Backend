package com.example.multi_tanent.purchases.service;

import org.springframework.stereotype.Service;

import com.example.multi_tanent.purchases.entity.PurchaseBill;
import com.example.multi_tanent.purchases.entity.PurPurchaseOrder;
import com.example.multi_tanent.purchases.repository.PurchaseBillRepository;
import com.example.multi_tanent.purchases.repository.PurPurchaseOrderRepository;

import java.util.List;

@Service
public class PurchaseBillService {
    private final PurchaseBillRepository billRepo;
    private final PurPurchaseOrderRepository poRepo;

    public PurchaseBillService(PurchaseBillRepository billRepo, PurPurchaseOrderRepository poRepo) {
        this.billRepo = billRepo;
        this.poRepo = poRepo;
    }

    public List<PurchaseBill> getAllBills() {
        return billRepo.findAll();
    }

    public PurchaseBill getBillById(Long id) {
        return billRepo.findById(id).orElse(null);
    }

    public PurchaseBill createBill(PurchaseBill bill, Long purchaseOrderId) {
        if (bill.getBillNo() == null || bill.getBillNo().isEmpty()) {
            long count = billRepo.count() + 1;
            bill.setBillNo(String.format("BILL-%04d", count));
        }

        if (purchaseOrderId != null) {
            PurPurchaseOrder po = poRepo.findById(purchaseOrderId).orElse(null);
            bill.setPurchaseOrder(po);
        }

        bill.setStatus("Pending");
        return billRepo.save(bill);
    }

    public PurchaseBill updateBill(Long id, PurchaseBill updated) {
        PurchaseBill existing = getBillById(id);
        if (existing == null) return null;

        updated.setId(id);
        if (updated.getBillNo() == null)
            updated.setBillNo(existing.getBillNo());

        return billRepo.save(updated);
    }

    public void deleteBill(Long id) {
        billRepo.deleteById(id);
    }
}
