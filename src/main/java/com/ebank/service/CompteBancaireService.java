package com.ebank.service;

import com.ebank.dto.compte.CompteBancaireMinimalDTO;
import com.ebank.dto.compte.CompteRequestDTO;
import com.ebank.dto.compte.CompteResponseDTO;

import java.util.List;

public interface CompteBancaireService {
    CompteResponseDTO createCompte(CompteRequestDTO request);

    CompteResponseDTO getCompteByRib(String rib);

    List<CompteBancaireMinimalDTO> getComptesByClientId(Long clientId);

    void bloquerCompte(Long compteId);
}
