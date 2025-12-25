package com.ebank.service;

import com.ebank.dto.operation.OperationResponseDTO;
import com.ebank.dto.operation.VirementRequestDTO;

public interface VirementService {
    OperationResponseDTO effectuerVirement(VirementRequestDTO request, String clientEmail);

    org.springframework.data.domain.Page<OperationResponseDTO> getHistorique(String clientEmail, int page, int size);
}
