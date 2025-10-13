package com.example.demo.service;

import com.example.demo.model.Folder;
import com.example.demo.repository.FolderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FolderService {

    private final FolderRepository folderRepository;

    public FolderService(FolderRepository folderRepository) {
        this.folderRepository = folderRepository;
    }

    public List<Folder> findAll() {
        return folderRepository.findAll();
    }

    public Optional<Folder> findById(Long id) {
        return folderRepository.findById(id);
    }

    public Folder save(Folder folder) {
        return folderRepository.save(folder);
    }

    public Folder update(Long id, Folder folderDetails) {
        return folderRepository.findById(id)
                .map(folder -> {
                    folder.setType(folderDetails.getType());
                    folder.setDateGenerate(folderDetails.getDateGenerate());
                    folder.setDocuments(folderDetails.getDocuments());
                    folder.setReglePaiement(folderDetails.getReglePaiement());
                    folder.setCompany(folderDetails.getCompany());
                    return folderRepository.save(folder);
                })
                .orElseThrow(() -> new RuntimeException("Folder not found with id: " + id));
    }

    public void delete(Long id) {
        if (!folderRepository.existsById(id)) {
            throw new RuntimeException("Folder not found with id: " + id);
        }
        folderRepository.deleteById(id);
    }
}
