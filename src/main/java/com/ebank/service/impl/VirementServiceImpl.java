package com.ebank.service.impl;

import com.ebank.dto.operation.OperationResponseDTO;
import com.ebank.dto.operation.VirementRequestDTO;
import com.ebank.entity.Client;
import com.ebank.entity.CompteBancaire;
import com.ebank.entity.Operation;
import com.ebank.entity.enums.StatutCompte;
import com.ebank.entity.enums.TypeOperation;
import com.ebank.exception.OperationException;
import com.ebank.exception.ResourceNotFoundException;
import com.ebank.mapper.OperationMapper;
import com.ebank.repository.ClientRepository;
import com.ebank.repository.CompteBancaireRepository;
import com.ebank.repository.OperationRepository;
import com.ebank.service.EmailService;
import com.ebank.service.VirementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@PreAuthorize("hasRole('CLIENT')")
public class VirementServiceImpl implements VirementService {

        private static final Logger log = LoggerFactory.getLogger(VirementServiceImpl.class);

        private final CompteBancaireRepository compteRepository;
        private final OperationRepository operationRepository;
        private final ClientRepository clientRepository;
        private final OperationMapper operationMapper;
        private final EmailService emailService;

        public VirementServiceImpl(CompteBancaireRepository compteRepository,
                        OperationRepository operationRepository,
                        ClientRepository clientRepository,
                        OperationMapper operationMapper,
                        EmailService emailService) {
                this.compteRepository = compteRepository;
                this.operationRepository = operationRepository;
                this.clientRepository = clientRepository;
                this.operationMapper = operationMapper;
                this.emailService = emailService;
        }

        @Override
        @Transactional
        public OperationResponseDTO effectuerVirement(VirementRequestDTO request, String clientEmail) {
                log.info("Processing transfer request from {} to {}", request.getRibSource(),
                                request.getRibDestinataire());

                // 1. Validate Client
                Client client = clientRepository.findByEmail(clientEmail)
                                .orElseThrow(() -> new ResourceNotFoundException("Client non authentifié"));

                // 2. Validate Source Account (Must belong to client)
                CompteBancaire source = compteRepository.findByRib(request.getRibSource())
                                .orElseThrow(() -> new ResourceNotFoundException("Compte source non trouvé"));

                if (!source.getClient().getId().equals(client.getId())) {
                        throw new OperationException("Vous n'êtes pas propriétaire du compte source");
                }

                // RG_11: Check status
                if (source.getStatut() != StatutCompte.OUVERT) {
                        throw new OperationException("Le compte source est bloqué ou clôturé");
                }

                // 3. Validate Destination Account
                CompteBancaire destination = compteRepository.findByRib(request.getRibDestinataire())
                                .orElseThrow(() -> new ResourceNotFoundException("Compte destinataire non trouvé"));

                if (destination.getStatut() != StatutCompte.OUVERT) {
                        throw new OperationException("Le compte destinataire est bloqué ou clôturé");
                }

                /*
                 * Removed circular check per assumption that internal transfers to self are
                 * allowed,
                 * only blocking if same account
                 */
                if (source.getId().equals(destination.getId())) {
                        throw new OperationException("Impossible d'effectuer un virement sur le même compte");
                }

                // RG_12: Check Balance
                if (source.getSolde().compareTo(request.getMontant()) < 0) {
                        throw new OperationException("Solde insuffisant");
                }

                // RG_13 & RG_14: Debit/Credit
                source.setSolde(source.getSolde().subtract(request.getMontant()));
                destination.setSolde(destination.getSolde().add(request.getMontant()));

                compteRepository.save(source);
                compteRepository.save(destination);

                // RG_15: Create Operations
                LocalDateTime now = LocalDateTime.now();

                // Debit Operation
                Operation debitOp = new Operation();
                debitOp.setType(TypeOperation.DEBIT);
                debitOp.setMontant(request.getMontant());
                debitOp.setDateOperation(now);
                debitOp.setIntitule("Virement vers " + destination.getRib());
                debitOp.setMotif(request.getMotif());
                debitOp.setCompteSource(source);
                debitOp.setCompteDestination(destination);
                operationRepository.save(debitOp);

                // Credit Operation (System creates a mirror entry if needed for tracking,
                // but typically one Transaction record connecting both accounts is enough if
                // schema supports it.
                // Given the requirement "crée 2 opérations", I will create the Credit side as
                // well for the other user's history)

                Operation creditOp = new Operation();
                creditOp.setType(TypeOperation.CREDIT);
                creditOp.setMontant(request.getMontant());
                creditOp.setDateOperation(now);
                creditOp.setIntitule("Virement de " + source.getRib());
                creditOp.setMotif(request.getMotif());
                creditOp.setCompteSource(source);
                creditOp.setCompteDestination(destination);
                operationRepository.save(creditOp);

                // Notify
                emailService.sendTransferNotification(
                                client.getEmail(),
                                source.getRib(),
                                destination.getRib(),
                                request.getMontant(),
                                request.getMotif());

                log.info("Transfer completed successfully. Op ID: {}", debitOp.getId());
                return operationMapper.toDto(debitOp);
        }

        @Override
        @Transactional(readOnly = true)
        public org.springframework.data.domain.Page<OperationResponseDTO> getHistorique(String clientEmail, int page,
                        int size) {
                Client client = clientRepository.findByEmail(clientEmail)
                                .orElseThrow(() -> new ResourceNotFoundException("Client non trouvé"));

                org.springframework.data.domain.PageRequest pageRequest = org.springframework.data.domain.PageRequest
                                .of(page,
                                                size,
                                                org.springframework.data.domain.Sort.by("dateOperation").descending());

                return operationRepository.findByClientId(client.getId(), pageRequest)
                                .map(operationMapper::toDto);
        }
}
