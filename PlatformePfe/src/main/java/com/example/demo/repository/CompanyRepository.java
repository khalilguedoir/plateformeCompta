package com.example.demo.repository;

import com.example.demo.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company,Long> {
    boolean existsByName(String name);
    Optional<Company> findByName(String name);
}
