package com.ebank.repository;

import com.ebank.entity.CompteBancaire;
import com.ebank.entity.enums.StatutCompte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompteBancaireRepository extends JpaRepository<CompteBancaire, Long> {

    @Transactional(readOnly = true)
    Optional<CompteBancaire> findByRib(String rib);

    @Transactional(readOnly = true)
    List<CompteBancaire> findByClientId(Long clientId);

    @Transactional(readOnly = true)
    List<CompteBancaire> findByClientEmail(String email);

    @Transactional(readOnly = true)
    List<CompteBancaire> findByStatut(StatutCompte statut);

    @Transactional(readOnly = true)
    Optional<CompteBancaire> findByRibAndStatut(String rib, StatutCompte statut);

    // Trouver le compte avec la dernière opération pour un client
    @Transactional(readOnly = true)
    @Query("SELECT c FROM CompteBancaire c " +
            "WHERE c.client.id = :clientId " +
            "ORDER BY (SELECT MAX(o.dateOperation) FROM Operation o WHERE o.compteSource.id = c.id OR o.compteDestination.id = c.id) DESC")
    List<CompteBancaire> findMostRecentlyActiveByClientIdList(@Param("clientId") Long clientId);

    // Wrapper to get just the first one as Optional
    default Optional<CompteBancaire> findMostRecentlyActiveByClientId(Long clientId) {
        return findMostRecentlyActiveByClientIdList(clientId).stream().findFirst();
    }
}
