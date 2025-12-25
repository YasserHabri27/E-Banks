package com.ebank.controller;

import com.ebank.dto.client.ClientMinimalDTO;
import com.ebank.dto.client.ClientRequestDTO;
import com.ebank.dto.client.ClientResponseDTO;
import com.ebank.service.ClientService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agent/clients")
@PreAuthorize("hasRole('AGENT_GUICHET')")
public class ClientController {

    private static final Logger log = LoggerFactory.getLogger(ClientController.class);

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<ClientResponseDTO> createClient(@Valid @RequestBody ClientRequestDTO request) {
        log.info("Request to create client: {}", request.getEmail());
        return ResponseEntity.ok(clientService.createClient(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> getClient(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.findClientById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ClientMinimalDTO>> searchClients(@RequestParam String keyword) {
        return ResponseEntity.ok(clientService.searchClients(keyword));
    }

    @GetMapping
    public ResponseEntity<List<ClientMinimalDTO>> getAllClients() {
        return ResponseEntity.ok(clientService.searchClients(""));
    }
}
