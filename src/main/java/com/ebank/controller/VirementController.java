package com.ebank.controller;

import com.ebank.dto.operation.OperationResponseDTO;
import com.ebank.dto.operation.VirementRequestDTO;
import com.ebank.service.VirementService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/client/virements")
@PreAuthorize("hasRole('CLIENT')")
public class VirementController {

    private static final Logger log = LoggerFactory.getLogger(VirementController.class);

    private final VirementService virementService;

    public VirementController(VirementService virementService) {
        this.virementService = virementService;
    }

    @PostMapping
    public ResponseEntity<OperationResponseDTO> effectuerVirement(
            @Valid @RequestBody VirementRequestDTO request,
            Principal principal) {
        log.info("Request to transfer money from {}", principal.getName());
        return ResponseEntity.ok(virementService.effectuerVirement(request, principal.getName()));
    }

    @GetMapping("/historique")
    public ResponseEntity<org.springframework.data.domain.Page<OperationResponseDTO>> getHistorique(
            Principal principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(virementService.getHistorique(principal.getName(), page, size));
    }
}
