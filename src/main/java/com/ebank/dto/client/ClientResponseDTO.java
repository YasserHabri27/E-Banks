package com.ebank.dto.client;

import com.ebank.dto.compte.CompteBancaireMinimalDTO;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public class ClientResponseDTO {

    private Long id;
    private String nom;
    private String prenom;
    private String numeroIdentite;
    private String email;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dateCreation;

    private List<CompteBancaireMinimalDTO> comptes;

    public ClientResponseDTO() {
    }

    public ClientResponseDTO(Long id, String nom, String prenom, String numeroIdentite, String email,
            LocalDateTime dateCreation, List<CompteBancaireMinimalDTO> comptes) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.numeroIdentite = numeroIdentite;
        this.email = email;
        this.dateCreation = dateCreation;
        this.comptes = comptes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNumeroIdentite() {
        return numeroIdentite;
    }

    public void setNumeroIdentite(String numeroIdentite) {
        this.numeroIdentite = numeroIdentite;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public List<CompteBancaireMinimalDTO> getComptes() {
        return comptes;
    }

    public void setComptes(List<CompteBancaireMinimalDTO> comptes) {
        this.comptes = comptes;
    }
}
