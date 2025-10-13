package com.example.demo.model;

import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "folders")
public class Folder {
	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    private Date dateGenerate;
	    private String type;
	    @OneToMany(mappedBy = "folder", cascade = CascadeType.ALL, orphanRemoval = true)
	    private List<Document> documents;
	    
	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "reglepaiement_id", nullable = false)
	    private ReglePaiement reglePaiement;
	    
	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "company_id", nullable = false)
	    private Company company;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Date getDateGenerate() {
			return dateGenerate;
		}

		public void setDateGenerate(Date dateGenerate) {
			this.dateGenerate = dateGenerate;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public List<Document> getDocuments() {
			return documents;
		}

		public void setDocuments(List<Document> documents) {
			this.documents = documents;
		}

		public ReglePaiement getReglePaiement() {
			return reglePaiement;
		}

		public void setReglePaiement(ReglePaiement reglePaiement) {
			this.reglePaiement = reglePaiement;
		}

		public Company getCompany() {
			return company;
		}

		public void setCompany(Company company) {
			this.company = company;
		}
	    
	    
	    
	    
	    

	    
	    
	    
}
