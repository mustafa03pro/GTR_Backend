package com.example.multi_tanent.purchases.service;

import org.springframework.stereotype.Service;

import com.example.multi_tanent.purchases.entity.PurPurchaseOrder;
import com.example.multi_tanent.purchases.repository.PurPurchaseOrderRepository;

import java.util.List;

@Service
public class PurPurchaseOrderService {
    private final PurPurchaseOrderRepository orderRepo;

    public PurPurchaseOrderService(PurPurchaseOrderRepository orderRepo) {
        this.orderRepo = orderRepo;
    }

    public List<PurPurchaseOrder> getAll() { return orderRepo.findAll(); }

    public PurPurchaseOrder getById(Long id) {
        return orderRepo.findById(id).orElse(null);
    }

    public PurPurchaseOrder save(PurPurchaseOrder order) {
        if (order.getPoNumber() == null || order.getPoNumber().isEmpty()) {
            long count = orderRepo.count() + 1;
            order.setPoNumber(String.format("PO-%04d", count));
        }
        order.getItems().forEach(i -> i.setPurchaseOrder(order));
        return orderRepo.save(order);
    }

    public void delete(Long id) { orderRepo.deleteById(id); }
}




//
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.List;
//
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.erp.purchase.entity.PurchaseOrder;
//import com.erp.purchase.entity.PurchaseOrderItem;
//import com.erp.purchase.repository.PurchaseOrderRepository;
//
//import lombok.RequiredArgsConstructor;
//
//@Service
//@RequiredArgsConstructor
//@Transactional
//public class PurchaseOrderServiceImpl implements PurchaseOrderService {
//
//    private final PurchaseOrderRepository poRepository;
//
//    @Override
//    public PurchaseOrder create(PurchaseOrder po) {
//        // generate PO number if not provided
//        if (po.getPoNumber() == null || po.getPoNumber().isBlank()) {
//            po.setPoNumber(generatePoNumber());
//        }
//        // ensure items reference parent and compute totals
//        computeTotals(po);
//        po.getItems().forEach(item -> item.setPurchaseOrder(po));
//        return poRepository.save(po);
//    }
//
//    @Override
//    public PurchaseOrder update(Long id, PurchaseOrder po) {
//    	PurchaseOrder existing = poRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("PO not found: " + id));
//
//    	
//
//    	
//        
//        // update fields
//        existing.setSupplierName(po.getSupplierName());
//        existing.setLocation(po.getLocation());
//        existing.setReferenceNo(po.getReferenceNo());
//        existing.setPoDate(po.getPoDate());
//        existing.setRemark(po.getRemark());
//        existing.setProjectNumber(po.getProjectNumber());
//        existing.setPoType(po.getPoType());
//        existing.setDiscountAtItemLevel(po.getDiscountAtItemLevel());
//        existing.setDeliveryTo(po.getDeliveryTo());
//        existing.setCustomerDetails(po.getCustomerDetails());
//        existing.setOtherCharges(po.getOtherCharges());
//
//       
//        // clear old items
//   	 existing.getItems().clear();
//
//   	 // add new ones
//   	 if (po.getItems() != null) {
//   	     for (PurchaseOrderItem item : po.getItems()) {
//   	         existing.addItem(item); // this sets both sides
//   	     }
//   	 }
//        
//        computeTotals(existing);
//        return poRepository.save(existing);
//    }
//
//    @Override
//    public void delete(Long id) {
//        poRepository.deleteById(id);
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public PurchaseOrder getById(Long id) {
//        return poRepository.findById(id).orElse(null);
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public List<PurchaseOrder> getAll() {
//        return poRepository.findAll();
//    }
//
//    @Override
//    public String generatePoNumber() {
//        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
//        long count = poRepository.count() + 1;
//        return "PO-" + date + "-" + String.format("%04d", count);
//    }
//
//    // helper to compute totals
//    private void computeTotals(PurchaseOrder po) {
//        double subtotal = 0.0;
//        double totalDiscount = 0.0;
//        double totalTax = 0.0;
//
//        if (po.getItems() != null) {
//            for (PurchaseOrderItem it : po.getItems()) {
//                int qty = it.getQuantity() == null ? 0 : it.getQuantity();
//                double rate = it.getRate() == null ? 0.0 : it.getRate();
//                double amount = qty * rate;
//                double discount = amount * ((it.getDiscountPercent() == null ? 0.0 : it.getDiscountPercent()) / 100.0);
//                double taxable = amount - discount;
//                double tax = taxable * ((it.getTaxPercent() == null ? 0.0 : it.getTaxPercent()) / 100.0);
//                double finalAmount = taxable + tax;
//
//                it.setAmount(finalAmount);
//                subtotal += amount;
//                totalDiscount += discount;
//                totalTax += tax;
//            }
//        }
//        po.setSubtotal(subtotal);
//        po.setTotalDiscount(totalDiscount);
//        po.setTotalTax(totalTax);
//        double other = po.getOtherCharges() == null ? 0.0 : po.getOtherCharges();
//        po.setGrandTotal(subtotal - totalDiscount + totalTax + other);
//    }
//   
//
//}
