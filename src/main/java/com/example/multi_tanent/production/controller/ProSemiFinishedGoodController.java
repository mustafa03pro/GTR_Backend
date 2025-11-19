package com.example.multi_tanent.production.controller;

// import jakarta.validation.Valid;
// import lombok.RequiredArgsConstructor;
// import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.data.domain.*;
// import org.springframework.http.*;
// import org.springframework.web.bind.annotation.*;

// import com.example.multi_tanent.production.dto.ProSemiFinishedGoodRequest;
// import com.example.multi_tanent.production.dto.ProSemiFinishedGoodResponse;
// import com.example.multi_tanent.production.services.ProSemiFinishedGoodService;

// @RestController
// @RequestMapping("/api/products/semi-finished")
// @RequiredArgsConstructor
// @PreAuthorize("isAuthenticated()")
// @CrossOrigin(origins = "*")
// public class ProSemiFinishedGoodController {

//     private final ProSemiFinishedGoodService service;

//     @PostMapping
//     public ResponseEntity<ProSemiFinishedGoodResponse> create(@Valid @RequestBody ProSemiFinishedGoodRequest req) {
//         ProSemiFinishedGoodResponse resp = service.create(req);
//         return ResponseEntity.status(HttpStatus.CREATED).body(resp);
//     }

//     @GetMapping
//     public Page<ProSemiFinishedGoodResponse> list(
//             @RequestParam(defaultValue = "0") int page,
//             @RequestParam(defaultValue = "20") int size,
//             @RequestParam(defaultValue = "createdAt,desc") String sort // e.g. "createdAt,desc"
//     ) {
//         Sort s = Sort.by(Sort.Direction.DESC, "createdAt");
//         try {
//             String[] sp = sort.split(",");
//             s = Sort.by(Sort.Direction.fromString(sp[1]), sp[0]);
//         } catch (Exception ignored) {}
//         Pageable p = PageRequest.of(page, size, s);
//         return service.findAll(p);
//     }

//     @GetMapping("/{id}")
//     public ResponseEntity<ProSemiFinishedGoodResponse> getById(@PathVariable Long id) {
//         return ResponseEntity.ok(service.getById(id));
//     }

//     @PutMapping("/{id}")
//     public ResponseEntity<ProSemiFinishedGoodResponse> update(@PathVariable Long id,
//                                                               @Valid @RequestBody ProSemiFinishedGoodRequest req) {
//         return ResponseEntity.ok(service.update(id, req));
//     }

//     @DeleteMapping("/{id}")
//     public ResponseEntity<Void> delete(@PathVariable Long id) {
//         service.delete(id);
//         return ResponseEntity.noContent().build();
//     }
// }




import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.example.multi_tanent.production.dto.ProSemiFinishedGoodRequest;
import com.example.multi_tanent.production.dto.ProSemiFinishedGoodResponse;
import com.example.multi_tanent.production.services.ProSemiFinishedGoodService;

@RestController
@RequestMapping("/api/production/semi-finished")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@CrossOrigin(origins = "*")
public class ProSemiFinishedGoodController {

private final ProSemiFinishedGoodService service;

@PostMapping
public ResponseEntity<ProSemiFinishedGoodResponse> create(@Valid @RequestBody ProSemiFinishedGoodRequest req) {
 ProSemiFinishedGoodResponse resp = service.create(req);
 return ResponseEntity.status(HttpStatus.CREATED).body(resp);
}

    // =====================================================================
    // ✅ MODIFIED: list method now accepts an optional 'search' parameter
    // =====================================================================
@GetMapping
public Page<ProSemiFinishedGoodResponse> list(
@RequestParam(defaultValue = "0") int page,
 @RequestParam(defaultValue = "20") int size,
            // 💡 ADDED: Optional search term parameter
            @RequestParam(required = false) String search, 
 @RequestParam(defaultValue = "createdAt,desc") String sort // e.g. "createdAt,desc"
) {
 Sort s = Sort.by(Sort.Direction.DESC, "createdAt");
try {
 String[] sp = sort.split(",");
 s = Sort.by(Sort.Direction.fromString(sp[1]), sp[0]);
} catch (Exception ignored) {}
 Pageable p = PageRequest.of(page, size, s);
        
 return service.findAll(search, p); // 💡 Passed the search parameter to the service
}
    // =====================================================================

@GetMapping("/{id}")
 public ResponseEntity<ProSemiFinishedGoodResponse> getById(@PathVariable Long id) {
 return ResponseEntity.ok(service.getById(id));
 }

 @PutMapping("/{id}")
 public ResponseEntity<ProSemiFinishedGoodResponse> update(@PathVariable Long id,
@Valid @RequestBody ProSemiFinishedGoodRequest req) {
 return ResponseEntity.ok(service.update(id, req));
 }

 @DeleteMapping("/{id}")
 public ResponseEntity<Void> delete(@PathVariable Long id) {
 service.delete(id);
 return ResponseEntity.noContent().build();
 }
}