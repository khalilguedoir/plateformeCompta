package com.example.demo.service;

import com.example.demo.model.Document;
import com.example.demo.repository.DocumentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;

    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public List<Document> findAll() {
        return documentRepository.findAll();
    }

    public Optional<Document> findById(Long id) {
        return documentRepository.findById(id);
    }

    public Document save(Document document) {
        return documentRepository.save(document);
    }

    public Document update(Long id, Document documentDetails) {
        return documentRepository.findById(id)
                .map(document -> {
                    document.setType(documentDetails.getType());
                    document.setNumber(documentDetails.getNumber());
                    document.setDateUpload(documentDetails.getDateUpload());
                    document.setInvoice(documentDetails.getInvoice());
                    document.setUrlFile(documentDetails.getUrlFile());
                    document.setFolder(documentDetails.getFolder());
                    return documentRepository.save(document);
                })
                .orElseThrow(() -> new RuntimeException("Document not found with id: " + id));
    }

    public void delete(Long id) {
        if (!documentRepository.existsById(id)) {
            throw new RuntimeException("Document not found with id: " + id);
        }
        documentRepository.deleteById(id);
    }
}
