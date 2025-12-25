package com.ebank.service.impl;

import com.ebank.dto.client.ClientMinimalDTO;
import com.ebank.dto.compte.CompteBancaireMinimalDTO;
import com.ebank.dto.operation.DashboardResponseDTO;
import com.ebank.dto.operation.OperationResponseDTO;
import com.ebank.entity.Client;
import com.ebank.entity.CompteBancaire;
import com.ebank.exception.ResourceNotFoundException;
import com.ebank.mapper.ClientMapper;
import com.ebank.mapper.CompteBancaireMapper;
import com.ebank.mapper.DashboardMapper;
import com.ebank.mapper.OperationMapper;
import com.ebank.repository.ClientRepository;
import com.ebank.repository.CompteBancaireRepository;
import com.ebank.repository.OperationRepository;
import com.ebank.service.DashboardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@PreAuthorize("hasRole('CLIENT')")
public class DashboardServiceImpl implements DashboardService {

        private final ClientRepository clientRepository;
        private final CompteBancaireRepository compteRepository;
        private final OperationRepository operationRepository;
        private final ClientMapper clientMapper;
        private final CompteBancaireMapper compteMapper;
        private final OperationMapper operationMapper;
        private final DashboardMapper dashboardMapper;

        public DashboardServiceImpl(ClientRepository clientRepository,
                        CompteBancaireRepository compteRepository,
                        OperationRepository operationRepository,
                        ClientMapper clientMapper,
                        CompteBancaireMapper compteMapper,
                        OperationMapper operationMapper,
                        DashboardMapper dashboardMapper) {
                this.clientRepository = clientRepository;
                this.compteRepository = compteRepository;
                this.operationRepository = operationRepository;
                this.clientMapper = clientMapper;
                this.compteMapper = compteMapper;
                this.operationMapper = operationMapper;
                this.dashboardMapper = dashboardMapper;
        }

        @Override
        @Transactional(readOnly = true)
        public DashboardResponseDTO getDashboard(String clientEmail) {
                Client client = clientRepository.findByEmail(clientEmail)
                                .orElseThrow(() -> new ResourceNotFoundException("Client non trouvé"));

                // 1. All Accounts
                List<CompteBancaire> comptesNodes = compteRepository.findByClientId(client.getId());
                List<CompteBancaireMinimalDTO> comptes = comptesNodes.stream()
                                .map(compteMapper::toMinimalDto)
                                .collect(Collectors.toList());

                // 2. Most Recently Active Account
                CompteBancaire activeAccount = compteRepository.findMostRecentlyActiveByClientId(client.getId())
                                .or(() -> comptesNodes.stream().findFirst()) // Fallback to first account
                                .orElse(null);

                // 3. Top 10 Operations (across all accounts)
                List<OperationResponseDTO> recentOperations = operationRepository.findTop10ByClientId(client.getId())
                                .stream()
                                .map(operationMapper::toDto)
                                .collect(Collectors.toList());

                // 4. Total Balance
                BigDecimal totalBalance = comptesNodes.stream()
                                .map(CompteBancaire::getSolde)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                // Mapper aggregation
                ClientMinimalDTO clientDto = clientMapper.toMinimalDto(client);
                CompteBancaireMinimalDTO activeAccountDto = activeAccount != null
                                ? compteMapper.toMinimalDto(activeAccount)
                                : null;

                return dashboardMapper.toDashboardDto(
                                clientDto,
                                activeAccountDto,
                                totalBalance,
                                comptes,
                                recentOperations,
                                0 // Total pages not relevant for dashboard view, only specific list view
                );
        }

        @Override
        @Transactional(readOnly = true)
        public Page<OperationResponseDTO> getOperationsByCompte(Long compteId, int page, int size) {
                PageRequest pageRequest = PageRequest.of(page, size, Sort.by("dateOperation").descending());

                // This query in Repo needs to handle Source OR Dest matching the account
                return operationRepository.findByCompteSourceIdOrCompteDestinationId(compteId, compteId, pageRequest)
                                .map(operationMapper::toDto);
        }
}
