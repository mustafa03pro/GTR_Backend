// package com.example.multi_tanent.purchases.controller;

// import com.example.multi_tanent.purchases.dto.*;
// import com.example.multi_tanent.purchases.service.PurPurchaseInvoiceService;
// import jakarta.validation.Valid;
// import lombok.RequiredArgsConstructor;
// import org.springframework.data.domain.*;
// import org.springframework.http.*;
// import org.springframework.web.bind.annotation.*;

// @RestController
// @RequestMapping("/api/purchase/invoices")
// @RequiredArgsConstructor
// @CrossOrigin(origins = "*")
// public class PurPurchaseInvoiceController {

//     private final PurPurchaseInvoiceService service;

//     @PostMapping
//     public ResponseEntity<PurPurchaseInvoiceResponse> create(@Valid @RequestBody PurPurchaseInvoiceRequest req) {
//         PurPurchaseInvoiceResponse resp = service.create(req);
//         return ResponseEntity.status(HttpStatus.CREATED).body(resp);
//     }

//     @GetMapping
//     public ResponseEntity<Page<PurPurchaseInvoiceResponse>> list(
//             @RequestParam(defaultValue = "0") int page,
//             @RequestParam(defaultValue = "20") int size,
//             @RequestParam(defaultValue = "createdAt,desc") String sort
//     ) {
//         Sort s = Sort.by(Sort.Direction.DESC, "createdAt");
//         try {
//             String[] sp = sort.split(",");
//             if (sp.length == 2) s = Sort.by(Sort.Direction.fromString(sp[1]), sp[0]);
//         } catch (Exception ignored) {}
//         Pageable p = PageRequest.of(page, size, s);
//         return ResponseEntity.ok(service.list(p));
//     }

//     @GetMapping("/{id}")
//     public ResponseEntity<PurPurchaseInvoiceResponse> getById(@PathVariable Long id) {
//         return ResponseEntity.ok(service.getById(id));
//     }

//     @PutMapping("/{id}")
//     public ResponseEntity<PurPurchaseInvoiceResponse> update(@PathVariable Long id,
//                                                              @Valid @RequestBody PurPurchaseInvoiceRequest req) {
//         return ResponseEntity.ok(service.update(id, req));
//     }

//     @DeleteMapping("/{id}")
//     public ResponseEntity<Void> delete(@PathVariable Long id) {
//         service.delete(id);
//         return ResponseEntity.noContent().build();
//     }
// }

package com.example.multi_tanent.purchases.controller;

import com.example.multi_tanent.purchases.dto.*;
import com.example.multi_tanent.purchases.service.PurPurchaseInvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/purchases/invoices")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PurPurchaseInvoiceController {

    private final PurPurchaseInvoiceService service;

    @PostMapping
    public ResponseEntity<PurPurchaseInvoiceResponse> create(@Validated @RequestBody PurPurchaseInvoiceRequest req) {
        PurPurchaseInvoiceResponse resp = service.create(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @GetMapping
    public Page<PurPurchaseInvoiceResponse> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort
    ) {
        Sort s = Sort.by(Sort.Direction.DESC, "createdAt");
        try {
            String[] sp = sort.split(",");
            s = Sort.by(Sort.Direction.fromString(sp[1]), sp[0]);
        } catch (Exception ignored) {}
        Pageable p = PageRequest.of(page, size, s);
        return service.list(p);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurPurchaseInvoiceResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PurPurchaseInvoiceResponse> update(@PathVariable Long id,
                                                             @Validated @RequestBody PurPurchaseInvoiceRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
