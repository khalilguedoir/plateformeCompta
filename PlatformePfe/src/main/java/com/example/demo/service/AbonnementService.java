package com.example.demo.service;

import com.example.demo.dto.AbonnementRequestDTO;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

@Service
public class AbonnementService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private AbonnementRepository abonnementRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    // creation  abonnement && un utilisateur pour  la société
    public Abonnement createAbonnementWithUser(AbonnementRequestDTO dto) {

        // creation  société
        Company company = new Company();
        company.setName(dto.getCompanyName());
        company.setMatriculeFiscale(dto.getMatriculeFiscale());
        company.setDatabaseName(dto.getCompanyName().toLowerCase().replaceAll(" ", "_"));
        companyRepository.save(company);

        // creation abonnement
        Abonnement abonnement = new Abonnement();
        abonnement.setCompany(company);
        abonnement.setType(TypeAbonnement.valueOf(dto.getTypeAbonnement()));
        abonnement.setMontant(dto.getMontant());
        abonnement.setStatutPaiement(StatutPaiement.EN_ATTENTE);
        abonnement.setDateDebut(new Date());
        abonnementRepository.save(abonnement);

        //  creation l'utilisateur principal societe
        String rawPassword = generateRandomPassword(8);
        User user = new User();
        user.setUsername(company.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(Role.COMPANY);
        user.setCompany(company);
        user.setActive(false); // désactivé  validation par admin
        userRepository.save(user);

        //  email  confirmation
        String subject = "Votre compte a été créé - Abonnement en attente de validation";
        String body = "Bonjour,\n\nVotre compte a été créé avec succès.\n\n" +
                "Nom d'utilisateur : " + user.getUsername() + "\n" +
                "Email : " + user.getEmail() + "\n" +
                "Mot de passe : " + rawPassword + "\n\n" +
                "Votre abonnement est en attente de validation par l'administrateur.\n\n" +
                "Cordialement,\nL'équipe Plateforme";
        emailService.sendEmail(dto.getEmail(), subject, body);

        return abonnement;
    }

    // 🔐 Générer un mot de passe aléatoire
    private String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
