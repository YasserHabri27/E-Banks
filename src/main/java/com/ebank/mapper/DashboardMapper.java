package com.ebank.mapper;

import com.ebank.dto.client.ClientMinimalDTO;
import com.ebank.dto.compte.CompteBancaireMinimalDTO;
import com.ebank.dto.operation.DashboardResponseDTO;
import com.ebank.dto.operation.OperationResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DashboardMapper {

    @Mapping(target = "client", source = "client")
    @Mapping(target = "compteActif", source = "compteActif")
    @Mapping(target = "soldeTotal", source = "soldeTotal")
    @Mapping(target = "comptes", source = "comptes")
    @Mapping(target = "dernieresOperations", source = "dernieresOperations")
    @Mapping(target = "totalPages", source = "totalPages")
    DashboardResponseDTO toDashboardDto(ClientMinimalDTO client,
            CompteBancaireMinimalDTO compteActif,
            BigDecimal soldeTotal,
            List<CompteBancaireMinimalDTO> comptes,
            List<OperationResponseDTO> dernieresOperations,
            int totalPages);
}
