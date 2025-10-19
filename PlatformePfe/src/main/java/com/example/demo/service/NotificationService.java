package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.repository.AbonnementRepository;
import com.example.demo.repository.CompanyRepository;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final AbonnementRepository abonnementRepository;
    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(AbonnementRepository abonnementRepository, NotificationRepository notificationRepository) {
        this.abonnementRepository = abonnementRepository;
        this.notificationRepository = notificationRepository;
    }
    @Scheduled(cron = "0 0 9 * * *")
    public void verifierAbonnements() {
        System.out.println("[Scheduler] Vérification des abonnements : " + new Date());

        Date now = new Date();

        for (Abonnement ab : abonnementRepository.findAll()) {
            if (ab.getDateFin() == null || ab.getCompany() == null) continue;

            long diff = ab.getDateFin().getTime() - now.getTime();
            long days = TimeUnit.MILLISECONDS.toDays(diff);

            // Paiement non effectué
            if (ab.getStatutPaiement() == StatutPaiement.NON_PAYE) {
                String msg = "Paiement en attente pour l’abonnement de " + ab.getCompany().getName();

                if (!notificationRepository.existsByMessageAndCompany(msg, ab.getCompany())) {
                    Notification notif = new Notification(
                            "Alerte Paiement",
                            msg,
                            "ALERT",
                            ab.getCompany()
                    );
                    notificationRepository.save(notif);
                }
            }

            else if (days <= 5 && days >= 0) {
                String msg = "Votre abonnement expire dans " + days + " jours.";

                if (!notificationRepository.existsByMessageAndCompany(msg, ab.getCompany())) {
                    Notification notif = new Notification(
                            "Expiration Abonnement",
                            msg,
                            "WARNING",
                            ab.getCompany()
                    );
                    notificationRepository.save(notif);
                }
            }
        }
    }
}
