package com.example.multi_tanent.purchases.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.multi_tanent.purchases.entity.PurchaseBill;
import com.example.multi_tanent.purchases.entity.PurchasePayment;
import com.example.multi_tanent.purchases.repository.PurchaseBillRepository;
import com.example.multi_tanent.purchases.repository.PurchasePaymentRepository;


@Service
public class PurchasePaymentService {

    private final PurchasePaymentRepository paymentRepo;
    private final PurchaseBillRepository billRepo;

    public PurchasePaymentService(PurchasePaymentRepository paymentRepo, PurchaseBillRepository billRepo) {
        this.paymentRepo = paymentRepo;
        this.billRepo = billRepo;
    }

    public List<PurchasePayment> getAllPayments() {
        return paymentRepo.findAll();
    }

    public PurchasePayment getPaymentById(Long id) {
        return paymentRepo.findById(id).orElse(null);
    }

    public PurchasePayment createPayment(PurchasePayment payment, Long billId) {
        if (payment.getPaymentNumber() == null || payment.getPaymentNumber().isEmpty()) {
            long count = paymentRepo.count() + 1;
            payment.setPaymentNumber(String.format("PAY-%04d", count));
        }

        if (billId != null) {
            PurchaseBill bill = billRepo.findById(billId).orElse(null);
            payment.setBill(bill);
        }

        return paymentRepo.save(payment);
    }

    public PurchasePayment updatePayment(Long id, PurchasePayment updated) {
        PurchasePayment existing = getPaymentById(id);
        if (existing == null) return null;

        updated.setId(id);
        if (updated.getPaymentNumber() == null)
            updated.setPaymentNumber(existing.getPaymentNumber());

        return paymentRepo.save(updated);
    }

    public void deletePayment(Long id) {
        paymentRepo.deleteById(id);
    }
}
