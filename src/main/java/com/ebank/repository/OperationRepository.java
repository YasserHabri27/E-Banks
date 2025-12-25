package com.ebank.repository;

import com.ebank.entity.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {

    // Simple history by account IDs
    @Transactional(readOnly = true)
    List<Operation> findByCompteSourceIdOrCompteDestinationIdOrderByDateOperationDesc(Long sourceId, Long destId);

    // Top 10 by account
    @Transactional(readOnly = true)
    List<Operation> findTop10ByCompteSourceIdOrCompteDestinationIdOrderByDateOperationDesc(Long sourceId, Long destId);

    // 10 last operations for a CLIENT (across all their accounts)
    @Transactional(readOnly = true)
    @Query("SELECT o FROM Operation o WHERE o.compteSource.client.id = :clientId OR o.compteDestination.client.id = :clientId ORDER BY o.dateOperation DESC LIMIT 10")
    List<Operation> findTop10ByClientId(@Param("clientId") Long clientId);

    // Pagination across all client accounts
    @Transactional(readOnly = true)
    @Query("SELECT o FROM Operation o WHERE o.compteSource.client.id = :clientId OR o.compteDestination.client.id = :clientId")
    Page<Operation> findByClientId(@Param("clientId") Long clientId, Pageable pageable);

    // Pagination for specific account
    @Transactional(readOnly = true)
    Page<Operation> findByCompteSourceIdOrCompteDestinationId(Long sourceId, Long destId, Pageable pageable);
}
