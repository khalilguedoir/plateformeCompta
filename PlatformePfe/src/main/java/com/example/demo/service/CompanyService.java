package com.example.demo.service;

import com.example.demo.model.Company;
import com.example.demo.repository.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    public Optional<Company> findById(Long id) {
        return companyRepository.findById(id);
    }

    public Company save(Company company) {
        if (companyRepository.existsByMatriculeFiscale(company.getMatriculeFiscale())) {
            throw new IllegalArgumentException("Matricule fiscale déjà utilisée !");
        }
        return companyRepository.save(company);
    }

    public Company update(Long id, Company companyDetails) {
        return companyRepository.findById(id).map(company -> {
            company.setName(companyDetails.getName());
            company.setDatabaseName(companyDetails.getDatabaseName());
            company.setMatriculeFiscale(companyDetails.getMatriculeFiscale());
            company.setAddress(companyDetails.getAddress());
            company.setTimezone(companyDetails.getTimezone());
            return companyRepository.save(company);
        }).orElseThrow(() -> new RuntimeException("Company introuvable avec id: " + id));
    }

    public void delete(Long id) {
        if (!companyRepository.existsById(id)) {
            throw new RuntimeException("Company introuvable avec id: " + id);
        }
        companyRepository.deleteById(id);
    }
}
