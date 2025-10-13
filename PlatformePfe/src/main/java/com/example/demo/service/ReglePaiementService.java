package com.example.demo.service;

import com.example.demo.model.ReglePaiement;
import com.example.demo.repository.ReglePaiementRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReglePaiementService {

    private final ReglePaiementRepository reglePaiementRepository;

    public ReglePaiementService(ReglePaiementRepository reglePaiementRepository) {
        this.reglePaiementRepository = reglePaiementRepository;
    }

    public List<ReglePaiement> findAll() {
        return reglePaiementRepository.findAll();
    }

    public Optional<ReglePaiement> findById(Long id) {
        return reglePaiementRepository.findById(id);
    }

    public ReglePaiement save(ReglePaiement reglePaiement) {
        return reglePaiementRepository.save(reglePaiement);
    }

    public ReglePaiement update(Long id, ReglePaiement regleDetails) {
        return reglePaiementRepository.findById(id)
                .map(regle -> {
                    regle.setType(regleDetails.getType());
                    regle.setMontant(regleDetails.getMontant());
                    regle.setDate(regleDetails.getDate());
                    regle.setStatus(regleDetails.getStatus());
                    regle.setFolder(regleDetails.getFolder());
                    regle.setCompany(regleDetails.getCompany());
                    return reglePaiementRepository.save(regle);
                })
                .orElseThrow(() -> new RuntimeException("ReglePaiement not found with id: " + id));
    }

    public void delete(Long id) {
        if (!reglePaiementRepository.existsById(id)) {
            throw new RuntimeException("ReglePaiement not found with id: " + id);
        }
        reglePaiementRepository.deleteById(id);
    }
}
