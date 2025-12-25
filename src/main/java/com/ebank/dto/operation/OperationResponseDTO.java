package com.ebank.dto.operation;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OperationResponseDTO {

    private Long id;
    private String type;
    private BigDecimal montant;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dateOperation;

    private String intitule;
    private String ribCompteSource;
    private String ribCompteDestination;
    private String motif;

    public OperationResponseDTO() {
    }

    public OperationResponseDTO(Long id, String type, BigDecimal montant, LocalDateTime dateOperation, String intitule,
            String ribCompteSource, String ribCompteDestination, String motif) {
        this.id = id;
        this.type = type;
        this.montant = montant;
        this.dateOperation = dateOperation;
        this.intitule = intitule;
        this.ribCompteSource = ribCompteSource;
        this.ribCompteDestination = ribCompteDestination;
        this.motif = motif;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public LocalDateTime getDateOperation() {
        return dateOperation;
    }

    public void setDateOperation(LocalDateTime dateOperation) {
        this.dateOperation = dateOperation;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public String getRibCompteSource() {
        return ribCompteSource;
    }

    public void setRibCompteSource(String ribCompteSource) {
        this.ribCompteSource = ribCompteSource;
    }

    public String getRibCompteDestination() {
        return ribCompteDestination;
    }

    public void setRibCompteDestination(String ribCompteDestination) {
        this.ribCompteDestination = ribCompteDestination;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }
}
