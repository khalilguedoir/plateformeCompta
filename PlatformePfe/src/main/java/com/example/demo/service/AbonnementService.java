package com.example.demo.service;

import com.example.demo.dto.AbonnementRequestDTO;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private AddressRepository addressRepository;

    @Autowired
    private TimezoneRepository timezoneRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    /**
     * Crée une société, un abonnement et l'utilisateur principal avec adresse et timezone.
     */
    @Transactional
    public Abonnement createAbonnementWithUser(AbonnementRequestDTO dto) {

        // 1️⃣ Créer la société
        Company company = new Company();
        company.setName(dto.getCompanyName());
        company.setMatriculeFiscale(dto.getMatriculeFiscale());
        company.setDatabaseName(dto.getCompanyName().toLowerCase().replaceAll(" ", "_"));
        company.setEmail(dto.getEmail());

        // Récupérer la timezone
        Timezone timezone = timezoneRepository.findById(dto.getTimezoneId())
        		.orElseThrow(() -> new RuntimeException("Timezone introuvable"));
        company.setTimezone(timezone);

        companyRepository.save(company); // il faut d'abord sauvegarder pour avoir l'id

        // 2️⃣ Créer l'adresse
        Address address = new Address();
        address.setCity(dto.getAddress());
        address.setStreet(""); // si tu veux un champ street dans DTO
        address.setEntityName("Company");
        address.setEntityId(company.getId());

        // On prend un pays par défaut (ex: Tunisia)
        Country country = countryRepository.findById(1L) // par défaut Tunisia
                .orElseThrow(() -> new RuntimeException("Country introuvable"));
        address.setCountry(country);

        addressRepository.save(address);
        company.setAddress(address); // relier l'adresse à la société

        companyRepository.save(company); // mettre à jour company avec address

        // 3️⃣ Créer l'abonnement
        Abonnement abonnement = new Abonnement();
        abonnement.setCompany(company);
        abonnement.setType(TypeAbonnement.valueOf(dto.getTypeAbonnement()));
        abonnement.setMontant(dto.getMontant());
        abonnement.setStatutPaiement(StatutPaiement.EN_ATTENTE);
        abonnement.setDateDebut(new Date());

        abonnementRepository.save(abonnement);

        // 4️⃣ Créer l'utilisateur principal
        String rawPassword = generateRandomPassword(8);
        User user = new User();
        user.setUsername(company.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(Role.COMPANY);
        user.setCompany(company);
        user.setActive(false); // désactivé jusqu'à validation admin
        userRepository.save(user);

        // 5️⃣ Envoyer email
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
