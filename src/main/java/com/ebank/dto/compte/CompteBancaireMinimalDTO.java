package com.ebank.dto.compte;

import java.math.BigDecimal;

public class CompteBancaireMinimalDTO {
    private Long id;
    private String rib;
    private BigDecimal solde;
    private String statut;

    public CompteBancaireMinimalDTO() {
    }

    public CompteBancaireMinimalDTO(Long id, String rib, BigDecimal solde, String statut) {
        this.id = id;
        this.rib = rib;
        this.solde = solde;
        this.statut = statut;
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
}
