package com.ebank.service.impl;

import com.ebank.dto.compte.CompteBancaireMinimalDTO;
import com.ebank.dto.compte.CompteRequestDTO;
import com.ebank.dto.compte.CompteResponseDTO;
import com.ebank.entity.Client;
import com.ebank.entity.CompteBancaire;
import com.ebank.entity.enums.StatutCompte;
import com.ebank.exception.ResourceNotFoundException;
import com.ebank.exception.ValidationException;
import com.ebank.mapper.CompteBancaireMapper;
import com.ebank.repository.ClientRepository;
import com.ebank.repository.CompteBancaireRepository;
import com.ebank.service.CompteBancaireService;
import com.ebank.util.RIBGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@PreAuthorize("hasRole('AGENT_GUICHET')")
public class CompteBancaireServiceImpl implements CompteBancaireService {

    private static final Logger log = LoggerFactory.getLogger(CompteBancaireServiceImpl.class);

    private final CompteBancaireRepository compteRepository;
    private final ClientRepository clientRepository;
    private final CompteBancaireMapper compteMapper;
    private final RIBGenerator ribGenerator;

    public CompteBancaireServiceImpl(CompteBancaireRepository compteRepository,
            ClientRepository clientRepository,
            CompteBancaireMapper compteMapper,
            RIBGenerator ribGenerator) {
        this.compteRepository = compteRepository;
        this.clientRepository = clientRepository;
        this.compteMapper = compteMapper;
        this.ribGenerator = ribGenerator;
    }

    @Override
    @Transactional
    public CompteResponseDTO createCompte(CompteRequestDTO request) {
        log.info("Creating new account for client ID: {}", request.getClientId());

        Client client;
        if (request.getClientId() != null) {
            client = clientRepository.findById(request.getClientId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Client non trouvé avec l'ID: " + request.getClientId()));
        } else if (request.getNumeroIdentiteClient() != null) {
            client = clientRepository.findByNumeroIdentite(request.getNumeroIdentiteClient())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Client non trouvé avec le NI: " + request.getNumeroIdentiteClient()));
        } else {
            throw new ValidationException("L'identifiant du client est obligatoire");
        }

        CompteBancaire compte = compteMapper.toEntity(request);
        compte.setClient(client);

        // RG_9: RIB
        if (request.getRib() == null || request.getRib().isEmpty()) {
            String rib = ribGenerator.generateRib();
            // Ensure uniqueness
            while (compteRepository.findByRib(rib).isPresent()) {
                rib = ribGenerator.generateRib();
            }
            compte.setRib(rib);
        } else {
            if (compteRepository.findByRib(request.getRib()).isPresent()) {
                throw new ValidationException("Ce RIB existe déjà");
            }
            if (!ribGenerator.validateRib(request.getRib())) {
                throw new ValidationException("Format de RIB invalide");
            }
        }

        // RG_10: Default status is OUVERT
        compte.setStatut(StatutCompte.OUVERT);
        compte.setDateCreation(LocalDateTime.now());

        CompteBancaire savedCompte = compteRepository.save(compte);
        log.info("Account created successfully with RIB: {}", savedCompte.getRib());

        return compteMapper.toDto(savedCompte);
    }

    @Override
    @Transactional(readOnly = true)
    public CompteResponseDTO getCompteByRib(String rib) {
        CompteBancaire compte = compteRepository.findByRib(rib)
                .orElseThrow(() -> new ResourceNotFoundException("Compte non trouvé avec le RIB: " + rib));
        return compteMapper.toDto(compte);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompteBancaireMinimalDTO> getComptesByClientId(Long clientId) {
        if (!clientRepository.existsById(clientId)) {
            throw new ResourceNotFoundException("Client non trouvé avec l'ID: " + clientId);
        }

        return compteRepository.findByClientId(clientId).stream()
                .map(compteMapper::toMinimalDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void bloquerCompte(Long compteId) {
        CompteBancaire compte = compteRepository.findById(compteId)
                .orElseThrow(() -> new ResourceNotFoundException("Compte non trouvé"));

        compte.setStatut(StatutCompte.BLOQUE);
        compteRepository.save(compte);
        log.info("Account {} blocked", compte.getRib());
    }
}
