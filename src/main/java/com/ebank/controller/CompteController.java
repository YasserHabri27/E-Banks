package com.ebank.controller;

import com.ebank.dto.compte.CompteBancaireMinimalDTO;
import com.ebank.dto.compte.CompteRequestDTO;
import com.ebank.dto.compte.CompteResponseDTO;
import com.ebank.service.CompteBancaireService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agent/comptes")
@PreAuthorize("hasRole('AGENT_GUICHET')")
public class CompteController {

    private static final Logger log = LoggerFactory.getLogger(CompteController.class);

    private final CompteBancaireService compteService;

    public CompteController(CompteBancaireService compteService) {
        this.compteService = compteService;
    }

    @PostMapping
    public ResponseEntity<CompteResponseDTO> createCompte(@Valid @RequestBody CompteRequestDTO request) {
        log.info("Request to create account for client: {}", request.getClientId());
        return ResponseEntity.ok(compteService.createCompte(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompteResponseDTO> getCompte(@PathVariable String id) {
        return ResponseEntity.ok(compteService.getCompteByRib(id));
    }

    @PutMapping("/{id}/bloquer")
    public ResponseEntity<Void> bloquerCompte(@PathVariable Long id) {
        log.info("Request to block account ID: {}", id);
        compteService.bloquerCompte(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<CompteBancaireMinimalDTO>> getComptesByClient(@PathVariable Long clientId) {
        return ResponseEntity.ok(compteService.getComptesByClientId(clientId));
    }
}
