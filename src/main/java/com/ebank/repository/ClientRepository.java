package com.ebank.repository;

import com.ebank.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    @Transactional(readOnly = true)
    Optional<Client> findByNumeroIdentite(String numeroIdentite);

    @Transactional(readOnly = true)
    boolean existsByNumeroIdentite(String numeroIdentite);

    @Transactional(readOnly = true)
    Optional<Client> findByEmail(String email);

    @Transactional(readOnly = true)
    boolean existsByEmail(String email);

    @Transactional(readOnly = true)
    List<Client> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(String nom, String prenom);
}
