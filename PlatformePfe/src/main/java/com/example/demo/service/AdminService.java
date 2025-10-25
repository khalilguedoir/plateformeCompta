package com.example.demo.service;

import com.example.demo.config.TenantService;
import com.example.demo.model.Company;
import com.example.demo.model.User;
import com.example.demo.repository.CompanyRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TenantService tenantService;

    /**
     * Active la société et crée dynamiquement son schéma
     */
    @Transactional
    public void activateCompany(Long companyId) {
        // 1️⃣ Récupérer la société
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Société introuvable avec ID : " + companyId));

        // 2️⃣ Créer le schéma s’il n’existe pas
        tenantService.createTenantForCompany(company);
        System.out.println("✅ Schéma créé pour : " + company.getDatabaseName());

        // 3️⃣ Activer l’utilisateur principal
        User user = userRepository.findByCompanyId(companyId)
                .orElseThrow(() -> new RuntimeException("Utilisateur principal introuvable pour cette société"));
        if (user != null) {
            user.setActive(true);
            userRepository.save(user);
            System.out.println("✅ Utilisateur activé : " + user.getEmail());
        } else {
            System.out.println("⚠️ Aucun utilisateur trouvé pour cette société.");
        }
    }
}
