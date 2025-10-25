package com.example.demo.service;
import jakarta.servlet.http.HttpServletRequest;
import com.example.demo.config.DataSourceConfig;
import com.example.demo.config.TenantContext;
import com.example.demo.config.TenantService;
import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.model.Company;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.CompanyRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.Date;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private CompanyRepository companyRepository;
    @Autowired private CustomUserDetailsService userDetailsService;
    @Autowired private UserRepository userRepository;
    @Autowired private CompanyService companyService;
    @Autowired private TenantService tenantService;
    @Autowired private JwtService jwtService;
    @Autowired private DataSourceConfig dataSourceConfig;
    @Autowired private BCryptPasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String jwtSecret;
    public AuthResponse register(RegisterRequest request) {
        String username = request.getUsername();
        String email = request.getEmail();
        String password = request.getPassword();
        Role role = request.getRole();

        // Vérifier si le username existe déjà
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists: " + username);
        }

        // Vérifier si le rôle est valide
        if (role != Role.ADMIN && role != Role.ACCOUNTANT) {
            throw new RuntimeException("Only ADMIN or ACCOUNTANT roles are allowed.");
        }

        Company company = null;
        String message;

        if (role == Role.ACCOUNTANT) {
            // Le comptable doit être rattaché à une company existante via le tenant header
            String tenant = TenantContext.getCurrentTenant();

            if (tenant == null || tenant.isEmpty()) {
                throw new RuntimeException("Tenant header (X-Tenant-ID) is required for accountant registration");
            }

            company = companyRepository.findByName(tenant)
                    .orElseThrow(() -> new RuntimeException("Company not found for tenant: " + tenant));

            message = "Accountant created successfully in schema '" + tenant + "'";
        } else {
            // Admin global (super admin)
            message = "Admin user created successfully";
        }

        // Création de l’utilisateur
        User user = new User();
        user.setUsername(username);
        user.setEmail(email); // ✅ correction du problème email null
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        user.setActive(true);
        user.setCompany(company);

        user = userRepository.save(user);

        // Si c’est un comptable, il faut aussi l’ajouter dans le schema de l’entreprise
        if (role == Role.ACCOUNTANT && company != null) {
            DataSource tenantDataSource = dataSourceConfig.createDataSource(company.getName());
            JdbcTemplate jdbcTemplate = new JdbcTemplate(tenantDataSource);

            jdbcTemplate.update(
                    "INSERT INTO users (username, email, password, role, active) VALUES (?, ?, ?, ?, ?)",
                    user.getUsername(),
                    user.getEmail(),
                    user.getPassword(),
                    role.ordinal(),
                    true
            );
        }

        // Génération du JWT
        String tenantName = (company != null) ? company.getName() : "admin_db";
        String token = jwtService.generateToken(
                userDetailsService.loadUserByUsername(username),
                tenantName
        );

        return new AuthResponse(token, message);
    }


    
    
    public AuthResponse login(AuthRequest request, HttpServletRequest httpRequest) {
        String tenant = request.getTenant();
        if (tenant == null || tenant.isEmpty()) {
            tenant = httpRequest.getHeader("X-Tenant-ID");
        }

        if (tenant == null || tenant.isEmpty()) {
            throw new RuntimeException("Tenant is required either in request body or X-Tenant-ID header");
        }

        TenantContext.setCurrentTenant(tenant);

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

        String token = jwtService.generateToken(userDetails, tenant);

        TenantContext.clear();

        return new AuthResponse(token, "Login successful for tenant '" + tenant + "'");
    }
}
