package com.example.demo.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "reglepaiements")
public class ReglePaiement {
	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    private Double montant;
	    private Date date;
	    private String type;
	    private String status;
	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "folder_id")
	    private Folder folder;
	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "company_id", nullable = false)
	    private Company company;
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public Double getMontant() {
			return montant;
		}
		public void setMontant(Double montant) {
			this.montant = montant;
		}
		public Date getDate() {
			return date;
		}
		public void setDate(Date date) {
			this.date = date;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}

		public Folder getFolder() {
			return folder;
		}
		public void setFolder(Folder folder) {
			this.folder = folder;
		}
		public Company getCompany() {
			return company;
		}
		public void setCompany(Company company) {
			this.company = company;
		}

	    
	    
}
