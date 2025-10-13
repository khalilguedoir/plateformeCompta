package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "accounting_codes", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountingCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codecompte", nullable = false, length = 20)
    private String codeCompte;   // ex : "1", "11", "111"

    @Column(nullable = false, length = 255)
    private String label;        // ex : "Actif", "Banques"

    // Relation hiérarchique (compte parent)
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private AccountingCode parent;

    // Chaque plan comptable est configuré par pays
    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodeCompte() {
		return codeCompte;
	}

	public void setCodeCompte(String codeCompte) {
		this.codeCompte = codeCompte;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public AccountingCode getParent() {
		return parent;
	}

	public void setParent(AccountingCode parent) {
		this.parent = parent;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}
    
    
    
    
    
    
    
    
    
}
