package com.example.multi_tanent.production.controller;



import com.example.multi_tanent.production.dto.ProBomRequest;
import com.example.multi_tanent.production.dto.ProBomResponse;
import com.example.multi_tanent.production.services.ProBomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/production/boms")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProBomController {

    private final ProBomService service;

    @PostMapping
    public ResponseEntity<ProBomResponse> create(@Valid @RequestBody ProBomRequest req) {
        ProBomResponse resp = service.create(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @GetMapping
    public Page<ProBomResponse> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort) {

        Sort s = Sort.by(Sort.Direction.DESC, "createdAt");
        try {
            String[] sp = sort.split(",");
            s = Sort.by(Sort.Direction.fromString(sp[1]), sp[0]);
        } catch (Exception ignored) {}

        Pageable p = PageRequest.of(page, size, s);
        return service.list(p);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProBomResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProBomResponse> update(@PathVariable Long id,
                                                 @Valid @RequestBody ProBomRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

