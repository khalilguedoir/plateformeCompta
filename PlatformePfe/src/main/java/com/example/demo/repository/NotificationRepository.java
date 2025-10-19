package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Company;
import com.example.demo.model.Notification;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByRecipientTypeOrderByCreatedAtDesc(String recipientType);

    List<Notification> findByRecipientIdOrderByCreatedAtDesc(String recipientId);
    boolean existsByMessageAndCompany(String message, Company company);

}
