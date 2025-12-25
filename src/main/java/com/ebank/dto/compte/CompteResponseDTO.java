package com.ebank.dto.compte;

import com.ebank.dto.client.ClientMinimalDTO;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CompteResponseDTO {

    private Long id;
    private String rib;
    private BigDecimal solde;
    private String statut;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dateCreation;

    private ClientMinimalDTO client;

    public CompteResponseDTO() {
    }

    public CompteResponseDTO(Long id, String rib, BigDecimal solde, String statut, LocalDateTime dateCreation,
            ClientMinimalDTO client) {
        this.id = id;
        this.rib = rib;
        this.solde = solde;
        this.statut = statut;
        this.dateCreation = dateCreation;
        this.client = client;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRib() {
        return rib;
    }

    public void setRib(String rib) {
        this.rib = rib;
    }

    public BigDecimal getSolde() {
        return solde;
    }

    public void setSolde(BigDecimal solde) {
        this.solde = solde;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public ClientMinimalDTO getClient() {
        return client;
    }

    public void setClient(ClientMinimalDTO client) {
        this.client = client;
    }
}
