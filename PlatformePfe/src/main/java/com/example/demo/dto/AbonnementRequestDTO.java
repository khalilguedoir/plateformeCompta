package com.example.demo.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AbonnementRequestDTO {

    // Infos société
    private String companyName;
    private String matriculeFiscale;
    private String email;  // email pour login et communication
    private String address; // on peut adapter selon ton entity Address
    private Long timezoneId;

    // Infos abonnement
    private String typeAbonnement; // BASIC, PREMIUM
    private Double montant;
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getMatriculeFiscale() {
		return matriculeFiscale;
	}
	public void setMatriculeFiscale(String matriculeFiscale) {
		this.matriculeFiscale = matriculeFiscale;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Long getTimezoneId() {
		return timezoneId;
	}
	public void setTimezoneId(Long timezoneId) {
		this.timezoneId = timezoneId;
	}
	public String getTypeAbonnement() {
		return typeAbonnement;
	}
	public void setTypeAbonnement(String typeAbonnement) {
		this.typeAbonnement = typeAbonnement;
	}
	public Double getMontant() {
		return montant;
	}
	public void setMontant(Double montant) {
		this.montant = montant;
	}

}
