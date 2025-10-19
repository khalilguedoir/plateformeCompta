package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "abonnements")
public class Abonnement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date dateDebut;
    private Date dateFin;

    @Enumerated(EnumType.STRING)
    private TypeAbonnement type; // BASIC, PREMIUM

    private Double montant;

    @Enumerated(EnumType.STRING)
    private StatutPaiement statutPaiement; // PAYE, NON_PAYE, EN_ATTENTE

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDateDebut() {
		return dateDebut;
	}

	public void setDateDebut(Date dateDebut) {
		this.dateDebut = dateDebut;
	}

	public Date getDateFin() {
		return dateFin;
	}

	public void setDateFin(Date dateFin) {
		this.dateFin = dateFin;
	}

	public TypeAbonnement getType() {
		return type;
	}

	public void setType(TypeAbonnement type) {
		this.type = type;
	}

	public Double getMontant() {
		return montant;
	}

	public void setMontant(Double montant) {
		this.montant = montant;
	}

	public StatutPaiement getStatutPaiement() {
		return statutPaiement;
	}

	public void setStatutPaiement(StatutPaiement statutPaiement) {
		this.statutPaiement = statutPaiement;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}
    
    
    
}
