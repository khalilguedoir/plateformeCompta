package com.example.demo.model;
import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Entity
@Table(name = "Accountant")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Accountant {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numeroAgrement; 
    private String specialite;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;


    // Un comptable peut manipuler plusieurs comptes comptables
    @ManyToMany
    @JoinTable(
        name = "accountant_accounting_codes",
        schema = "public",
        joinColumns = @JoinColumn(name = "accountant_id"),
        inverseJoinColumns = @JoinColumn(name = "accounting_code_id")
    )
    private Set<AccountingCode> accountingCodes;


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getNumeroAgrement() {
		return numeroAgrement;
	}


	public void setNumeroAgrement(String numeroAgrement) {
		this.numeroAgrement = numeroAgrement;
	}


	public String getSpecialite() {
		return specialite;
	}


	public void setSpecialite(String specialite) {
		this.specialite = specialite;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	public Company getCompany() {
		return company;
	}


	public void setCompany(Company company) {
		this.company = company;
	}


	public Set<AccountingCode> getAccountingCodes() {
		return accountingCodes;
	}


	public void setAccountingCodes(Set<AccountingCode> accountingCodes) {
		this.accountingCodes = accountingCodes;
	}
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
