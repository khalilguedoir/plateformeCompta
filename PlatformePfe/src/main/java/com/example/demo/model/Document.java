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
@Table(name = "documents")
public class Document {
	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    private String number;
	    private Date dateUpload;
	    private String type;
	    private String urlFile;
	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "invoice_id", nullable = false)
	    private Invoice invoice;
	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "reglepaiement_id", nullable = false)
	    private ReglePaiement reglePaiement;
	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "folder_id", nullable = false)
	    private Folder folder;
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getNumber() {
			return number;
		}
		public void setNumber(String number) {
			this.number = number;
		}
		public Date getDateUpload() {
			return dateUpload;
		}
		public void setDateUpload(Date dateUpload) {
			this.dateUpload = dateUpload;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getUrlFile() {
			return urlFile;
		}
		public void setUrlFile(String urlFile) {
			this.urlFile = urlFile;
		}
		public Invoice getInvoice() {
			return invoice;
		}
		public void setInvoice(Invoice invoice) {
			this.invoice = invoice;
		}
		public ReglePaiement getReglePaiement() {
			return reglePaiement;
		}
		public void setReglePaiement(ReglePaiement reglePaiement) {
			this.reglePaiement = reglePaiement;
		}
		public Folder getFolder() {
			return folder;
		}
		public void setFolder(Folder folder) {
			this.folder = folder;
		}
	    
	    
	    
}
