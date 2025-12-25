package com.ebank.service.impl;

import com.ebank.dto.client.ClientMinimalDTO;
import com.ebank.dto.client.ClientRequestDTO;
import com.ebank.dto.client.ClientResponseDTO;
import com.ebank.entity.Client;
import com.ebank.exception.ResourceNotFoundException;
import com.ebank.exception.ValidationException;
import com.ebank.mapper.ClientMapper;
import com.ebank.repository.ClientRepository;
import com.ebank.service.ClientService;
import com.ebank.service.EmailService;
import com.ebank.util.PasswordGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@PreAuthorize("hasRole('AGENT_GUICHET')")
public class ClientServiceImpl implements ClientService {

    private static final Logger log = LoggerFactory.getLogger(ClientServiceImpl.class);

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final PasswordEncoder passwordEncoder;
    private final PasswordGenerator passwordGenerator;
    private final EmailService emailService;

    public ClientServiceImpl(ClientRepository clientRepository,
            ClientMapper clientMapper,
            PasswordEncoder passwordEncoder,
            PasswordGenerator passwordGenerator,
            EmailService emailService) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
        this.passwordEncoder = passwordEncoder;
        this.passwordGenerator = passwordGenerator;
        this.emailService = emailService;
    }

    @Override
    @Transactional
    public ClientResponseDTO createClient(ClientRequestDTO clientRequestDTO) {
        if (clientRepository.existsByEmail(clientRequestDTO.getEmail())) {
            throw new ValidationException("Un client avec cet email existe déjà");
        }
        if (clientRepository.existsByNumeroIdentite(clientRequestDTO.getNumeroIdentite())) {
            throw new ValidationException("Un client avec ce numéro d'identité existe déjà");
        }

        // Generate password
        String rawPassword = passwordGenerator.generatePassword(12);
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // Map and set User fields
        Client client = clientMapper.toEntity(clientRequestDTO);
        client.setPassword(encodedPassword);
        client.setDateCreation(LocalDateTime.now());

        Client savedClient = clientRepository.save(client);

        // Send Email (RG_7)
        emailService.sendClientCredentials(
                savedClient.getEmail(),
                savedClient.getEmail(),
                rawPassword,
                savedClient.getPrenom() + " " + savedClient.getNom());

        ClientResponseDTO response = clientMapper.toDto(savedClient);
        log.info("Client created successfully with ID: {}", savedClient.getId());
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public ClientResponseDTO findClientById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client non trouvé avec l'ID: " + id));
        return clientMapper.toDto(client);
    }

    @Override
    @Transactional(readOnly = true)
    public ClientResponseDTO findClientByEmail(String email) {
        Client client = clientRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Client non trouvé avec l'email: " + email));
        return clientMapper.toDto(client);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClientMinimalDTO> searchClients(String keyword) {
        return clientRepository.findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(keyword, keyword)
                .stream()
                .map(clientMapper::toMinimalDto)
                .collect(Collectors.toList());
    }
}
