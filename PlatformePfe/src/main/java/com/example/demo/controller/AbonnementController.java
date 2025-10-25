package com.example.demo.controller;

import com.example.demo.dto.AbonnementRequestDTO;
import com.example.demo.model.Abonnement;
import com.example.demo.service.AbonnementService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/abonnements")
@RequiredArgsConstructor
public class AbonnementController {

    private final AbonnementService abonnementService;

    public AbonnementController(AbonnementService abonnementService) {
        this.abonnementService = abonnementService;
    }


    @PostMapping("/create")
    public ResponseEntity<?> createAbonnement(@RequestBody AbonnementRequestDTO requestDTO) {
        System.out.println("Received abonnement request: " + requestDTO);
        if (requestDTO.getTimezoneId() == null) {
            return ResponseEntity.badRequest().body("timezoneId is required");
        }
        try {
            Abonnement abonnement = abonnementService.createAbonnementWithUser(requestDTO);
            return ResponseEntity.ok(abonnement);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
