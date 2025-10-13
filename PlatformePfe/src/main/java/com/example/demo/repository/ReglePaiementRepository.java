package com.example.demo.repository;

import com.example.demo.model.ReglePaiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReglePaiementRepository extends JpaRepository<ReglePaiement, Long> {
}
