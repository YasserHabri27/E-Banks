package com.ebank.dto.operation;

import com.ebank.dto.client.ClientMinimalDTO;
import com.ebank.dto.compte.CompteBancaireMinimalDTO;

import java.math.BigDecimal;
import java.util.List;

public class DashboardResponseDTO {

    private ClientMinimalDTO client;
    private CompteBancaireMinimalDTO compteActif;
    private BigDecimal soldeTotal;
    private List<CompteBancaireMinimalDTO> comptes;
    private List<OperationResponseDTO> dernieresOperations;
    private int totalPages;

    public DashboardResponseDTO() {
    }

    public DashboardResponseDTO(ClientMinimalDTO client, CompteBancaireMinimalDTO compteActif, BigDecimal soldeTotal,
            List<CompteBancaireMinimalDTO> comptes, List<OperationResponseDTO> dernieresOperations, int totalPages) {
        this.client = client;
        this.compteActif = compteActif;
        this.soldeTotal = soldeTotal;
        this.comptes = comptes;
        this.dernieresOperations = dernieresOperations;
        this.totalPages = totalPages;
    }

    public ClientMinimalDTO getClient() {
        return client;
    }

    public void setClient(ClientMinimalDTO client) {
        this.client = client;
    }

    public CompteBancaireMinimalDTO getCompteActif() {
        return compteActif;
    }

    public void setCompteActif(CompteBancaireMinimalDTO compteActif) {
        this.compteActif = compteActif;
    }

    public BigDecimal getSoldeTotal() {
        return soldeTotal;
    }

    public void setSoldeTotal(BigDecimal soldeTotal) {
        this.soldeTotal = soldeTotal;
    }

    public List<CompteBancaireMinimalDTO> getComptes() {
        return comptes;
    }

    public void setComptes(List<CompteBancaireMinimalDTO> comptes) {
        this.comptes = comptes;
    }

    public List<OperationResponseDTO> getDernieresOperations() {
        return dernieresOperations;
    }

    public void setDernieresOperations(List<OperationResponseDTO> dernieresOperations) {
        this.dernieresOperations = dernieresOperations;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
