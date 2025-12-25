package com.ebank.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

/**
 * Client entity extending User. Represents a bank client.
 */
@Entity
@Table(name = "client", indexes = {
        @Index(name = "idx_client_identity", columnList = "numeroIdentite")
})
public class Client extends User {

    @NotNull
    @Size(min = 2, max = 50)
    private String nom;

    @NotNull
    @Size(min = 2, max = 50)
    private String prenom;

    // RG_4: Unique identity number
    @NotNull
    @Column(unique = true, nullable = false)
    private String numeroIdentite;

    // RG_5
    @NotNull
    private LocalDate dateNaissance;

    // RG_5
    @NotNull
    private String adressePostale;

    private String telephone;

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CompteBancaire> comptesBancaires;

    public Client() {
        super();
    }

    // Full constructor
    public Client(Long id, String email, String password, com.ebank.entity.enums.Role role, boolean enabled,
            java.time.LocalDateTime dateCreation, java.time.LocalDateTime dateModification,
            String nom, String prenom, String numeroIdentite, LocalDate dateNaissance, String adressePostale,
            String telephone, List<CompteBancaire> comptesBancaires) {
        super(id, email, password, role, enabled, dateCreation, dateModification);
        this.nom = nom;
        this.prenom = prenom;
        this.numeroIdentite = numeroIdentite;
        this.dateNaissance = dateNaissance;
        this.adressePostale = adressePostale;
        this.telephone = telephone;
        this.comptesBancaires = comptesBancaires;
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

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getAdressePostale() {
        return adressePostale;
    }

    public void setAdressePostale(String adressePostale) {
        this.adressePostale = adressePostale;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public List<CompteBancaire> getComptesBancaires() {
        return comptesBancaires;
    }

    public void setComptesBancaires(List<CompteBancaire> comptesBancaires) {
        this.comptesBancaires = comptesBancaires;
    }
}
