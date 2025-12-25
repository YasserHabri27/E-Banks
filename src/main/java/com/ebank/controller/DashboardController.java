package com.ebank.controller;

import com.ebank.dto.compte.CompteBancaireMinimalDTO;
import com.ebank.dto.operation.DashboardResponseDTO;
import com.ebank.dto.operation.OperationResponseDTO;
import com.ebank.service.CompteBancaireService;
import com.ebank.service.DashboardService;
import com.ebank.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/client")
@PreAuthorize("hasRole('CLIENT')")
public class DashboardController {

    private final DashboardService dashboardService;
    private final CompteBancaireService compteService;
    private final UserService userService;

    public DashboardController(DashboardService dashboardService, CompteBancaireService compteService,
            UserService userService) {
        this.dashboardService = dashboardService;
        this.compteService = compteService;
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardResponseDTO> getDashboard(Principal principal) {
        return ResponseEntity.ok(dashboardService.getDashboard(principal.getName()));
    }

    @GetMapping("/comptes")
    public ResponseEntity<List<CompteBancaireMinimalDTO>> getMyAccounts(Principal principal) {
        Long clientId = userService.findByEmail(principal.getName()).getId();
        return ResponseEntity.ok(compteService.getComptesByClientId(clientId));
    }

    @GetMapping("/operations")
    public ResponseEntity<Page<OperationResponseDTO>> getOperations(
            @RequestParam Long compteId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(dashboardService.getOperationsByCompte(compteId, page, size));
    }
}
