package com.ebank.dto.operation;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class VirementRequestDTO {

    private String ribSource;

    @NotBlank(message = "Destination RIB is required")
    private String ribDestinataire;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal montant;

    private String motif;

    public VirementRequestDTO() {
    }

    public VirementRequestDTO(String ribSource, String ribDestinataire, BigDecimal montant, String motif) {
        this.ribSource = ribSource;
        this.ribDestinataire = ribDestinataire;
        this.montant = montant;
        this.motif = motif;
    }

    public String getRibSource() {
        return ribSource;
    }

    public void setRibSource(String ribSource) {
        this.ribSource = ribSource;
    }

    public String getRibDestinataire() {
        return ribDestinataire;
    }

    public void setRibDestinataire(String ribDestinataire) {
        this.ribDestinataire = ribDestinataire;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }
}
