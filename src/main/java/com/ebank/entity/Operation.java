package com.ebank.entity;

import com.ebank.entity.enums.TypeOperation;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Operation entity for banking transactions.
 */
@Entity
@Table(name = "operation")
@EntityListeners(AuditingEntityListener.class)
public class Operation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TypeOperation type;

    @NotNull
    @Positive
    private BigDecimal montant;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime dateOperation;

    private String intitule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compte_source_id")
    private CompteBancaire compteSource;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compte_destination_id")
    private CompteBancaire compteDestination;

    private String motif;

    public Operation() {
    }

    public Operation(Long id, TypeOperation type, BigDecimal montant, LocalDateTime dateOperation, String intitule,
            CompteBancaire compteSource, CompteBancaire compteDestination, String motif) {
        this.id = id;
        this.type = type;
        this.montant = montant;
        this.dateOperation = dateOperation;
        this.intitule = intitule;
        this.compteSource = compteSource;
        this.compteDestination = compteDestination;
        this.motif = motif;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TypeOperation getType() {
        return type;
    }

    public void setType(TypeOperation type) {
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

    public CompteBancaire getCompteSource() {
        return compteSource;
    }

    public void setCompteSource(CompteBancaire compteSource) {
        this.compteSource = compteSource;
    }

    public CompteBancaire getCompteDestination() {
        return compteDestination;
    }

    public void setCompteDestination(CompteBancaire compteDestination) {
        this.compteDestination = compteDestination;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }
}
