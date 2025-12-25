package com.ebank.entity;

import com.ebank.entity.enums.StatutCompte;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Bank Account entity.
 */
@Entity
@Table(name = "compte_bancaire", indexes = {
        @Index(name = "idx_compte_rib", columnList = "rib")
})
@EntityListeners(AuditingEntityListener.class)
public class CompteBancaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true, nullable = false)
    private String rib;

    @DecimalMin(value = "0.0")
    private BigDecimal solde = BigDecimal.ZERO;

    @NotNull
    @Enumerated(EnumType.STRING)
    private StatutCompte statut = StatutCompte.OUVERT;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @OneToMany(mappedBy = "compteSource", fetch = FetchType.LAZY)
    private List<Operation> operations;

    public CompteBancaire() {
    }

    public CompteBancaire(Long id, String rib, BigDecimal solde, StatutCompte statut, LocalDateTime dateCreation,
            Client client, List<Operation> operations) {
        this.id = id;
        this.rib = rib;
        this.solde = solde != null ? solde : BigDecimal.ZERO;
        this.statut = statut != null ? statut : StatutCompte.OUVERT;
        this.dateCreation = dateCreation;
        this.client = client;
        this.operations = operations;
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

    public StatutCompte getStatut() {
        return statut;
    }

    public void setStatut(StatutCompte statut) {
        this.statut = statut;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<Operation> getOperations() {
        return operations;
    }

    public void setOperations(List<Operation> operations) {
        this.operations = operations;
    }
}
