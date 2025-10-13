package com.example.demo.service;

import com.example.demo.model.Supplier;
import com.example.demo.repository.SupplierRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    public List<Supplier> findAll() {
        return supplierRepository.findAll();
    }

    public Optional<Supplier> findById(Long id) {
        return supplierRepository.findById(id);
    }

    public Supplier save(Supplier supplier) {
        return supplierRepository.save(supplier);
    }

    public Supplier update(Long id, Supplier supplierDetails) {
        return supplierRepository.findById(id)
                .map(supplier -> {
                    supplier.setName(supplierDetails.getName());
                    supplier.setEmail(supplierDetails.getEmail());
                    supplier.setPhonenumber(supplierDetails.getPhonenumber());
                    supplier.setAdresse(supplierDetails.getAdresse());
                    supplier.setCompany(supplierDetails.getCompany());
                    return supplierRepository.save(supplier);
                })
                .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + id));
    }

    public void delete(Long id) {
        if (!supplierRepository.existsById(id)) {
            throw new RuntimeException("Supplier not found with id: " + id);
        }
        supplierRepository.deleteById(id);
    }
}
