package com.example.demo.service;

import com.example.demo.model.Invoice;
import com.example.demo.repository.InvoiceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public List<Invoice> findAll() {
        return invoiceRepository.findAll();
    }

    public Optional<Invoice> findById(Long id) {
        return invoiceRepository.findById(id);
    }

    public Invoice save(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    public Invoice update(Long id, Invoice invoiceDetails) {
        return invoiceRepository.findById(id)
                .map(invoice -> {
                    invoice.setNumber(invoiceDetails.getNumber());
                    invoice.setDate(invoiceDetails.getDate());
                    invoice.setType(invoiceDetails.getType());
                    invoice.setTva(invoiceDetails.getTva());
                    return invoiceRepository.save(invoice);
                })
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + id));
    }

    public void delete(Long id) {
        if (!invoiceRepository.existsById(id)) {
            throw new RuntimeException("Invoice not found with id: " + id);
        }
        invoiceRepository.deleteById(id);
    }
}
