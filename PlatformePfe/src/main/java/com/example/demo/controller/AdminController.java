package com.example.demo.controller;

import com.example.demo.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    /**
     * Activer la société et créer son schéma
     */
    @PostMapping("/activate/{companyId}")
    public ResponseEntity<String> activateCompany(@PathVariable Long companyId) {
        adminService.activateCompany(companyId);
        return ResponseEntity.ok("✅ Société activée et schéma créé avec succès !");
    }
}
