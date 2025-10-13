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
        Role role = request.getRole();
        Company company = null;
        String message;

        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists: " + username);
        }

        if (role == Role.COMPANY && request.getCompany() != null) {
            String companyName = request.getCompany().getName().toLowerCase();

            if (companyRepository.existsByName(companyName)) {
                throw new RuntimeException("Company already exists: " + companyName);
            }

            company = new Company();
            company.setName(companyName);

            company = companyService.save(company);

            tenantService.createSchemaIfNotExists(companyName);

            message = "Company created successfully with schema '" + companyName + "'";
        }
        else if (role == Role.ACCOUNTANT) {
            String tenant = TenantContext.getCurrentTenant();
            if (tenant == null || tenant.isEmpty()) {
                throw new RuntimeException("Tenant header (X-Tenant-ID) is required for accountant registration");
            }

            company = companyRepository.findByName(tenant)
                    .orElseThrow(() -> new RuntimeException("Company not found for tenant: " + tenant));

            message = "Accountant created successfully in schema '" + tenant + "'";
        }
        else if (role == Role.ADMIN) {
            message = "Admin user created successfully";
        } else {
            throw new RuntimeException("Invalid role or missing company information");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);
        user.setCompany(company);
        user = userRepository.save(user);

        if (company != null) {
            DataSource tenantDataSource = dataSourceConfig.createDataSource(company.getName());
            JdbcTemplate jdbcTemplate = new JdbcTemplate(tenantDataSource);

            jdbcTemplate.update(
                "INSERT INTO users (username, password, role) VALUES (?, ?, ?)",
                user.getUsername(),
                user.getPassword(),
                role.ordinal()
            );
        }

        String tenantName = company != null ? company.getName() : null;
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
