package com.ebank.service;

import com.ebank.dto.client.ClientMinimalDTO;
import com.ebank.dto.client.ClientRequestDTO;
import com.ebank.dto.client.ClientResponseDTO;

import java.util.List;

public interface ClientService {
    ClientResponseDTO createClient(ClientRequestDTO request);

    ClientResponseDTO findClientById(Long id);

    ClientResponseDTO findClientByEmail(String email);

    List<ClientMinimalDTO> searchClients(String keyword);
}
