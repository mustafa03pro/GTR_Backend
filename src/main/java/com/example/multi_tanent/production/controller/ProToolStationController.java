package com.example.multi_tanent.production.controller;

import com.example.multi_tanent.production.dto.ProToolStationRequest;
import com.example.multi_tanent.production.dto.ProToolStationResponse;
import com.example.multi_tanent.production.services.ProToolStationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/production/tool-stations")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class ProToolStationController {

    private final ProToolStationService service;

    @PostMapping
    public ResponseEntity<ProToolStationResponse> create(@Valid @RequestBody ProToolStationRequest request) {
        return new ResponseEntity<>(service.create(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProToolStationResponse>> getAllByToolId(@RequestParam Long toolId) {
        return ResponseEntity.ok(service.getAllByToolId(toolId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProToolStationResponse> update(@PathVariable Long id, @Valid @RequestBody ProToolStationRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
