package com.example.demo.controller;

import com.example.demo.model.ReglePaiement;
import com.example.demo.service.ReglePaiementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/regles-paiement")
public class ReglePaiementController {

    private final ReglePaiementService reglePaiementService;

    public ReglePaiementController(ReglePaiementService reglePaiementService) {
        this.reglePaiementService = reglePaiementService;
    }

    @GetMapping
    public ResponseEntity<List<ReglePaiement>> getAllReglesPaiement() {
        return ResponseEntity.ok(reglePaiementService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReglePaiement> getReglePaiementById(@PathVariable Long id) {
        return reglePaiementService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ReglePaiement> createReglePaiement(@RequestBody ReglePaiement reglePaiement) {
        return ResponseEntity.ok(reglePaiementService.save(reglePaiement));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReglePaiement> updateReglePaiement(@PathVariable Long id, @RequestBody ReglePaiement regleDetails) {
        return ResponseEntity.ok(reglePaiementService.update(id, regleDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReglePaiement(@PathVariable Long id) {
        reglePaiementService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
