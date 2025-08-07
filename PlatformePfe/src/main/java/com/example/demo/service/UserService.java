package com.example.demo.service;

import com.example.demo.model.Company;
import com.example.demo.model.User;
import com.example.demo.repository.CompanyRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepo;
    private CompanyRepository companyRepo;

    @Autowired
    public UserService(UserRepository userRepo, CompanyRepository companyRepo) {
        this.userRepo = userRepo;
        this.companyRepo = companyRepo;
    }

    public String assignAccountantToCompany(Long userId, Long companyId) {
        Optional<User> userOpt = userRepo.findById(userId);
        Optional<Company> compOpt = companyRepo.findById(companyId);

        if (userOpt.isEmpty() || compOpt.isEmpty()) return "User or company not found";

        User user = userOpt.get();
        Company company = compOpt.get();

        user.setCompany(company);
        userRepo.save(user);

        return "Accountant assigned to company " + company.getName();
    }
}
