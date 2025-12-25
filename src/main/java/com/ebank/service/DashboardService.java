package com.ebank.service;

import com.ebank.dto.operation.DashboardResponseDTO;
import com.ebank.dto.operation.OperationResponseDTO;
import org.springframework.data.domain.Page;

public interface DashboardService {
    DashboardResponseDTO getDashboard(String clientEmail);

    Page<OperationResponseDTO> getOperationsByCompte(Long compteId, int page, int size);
}
