package com.ebank.mapper;

import com.ebank.dto.operation.OperationResponseDTO;
import com.ebank.dto.operation.VirementRequestDTO;
import com.ebank.entity.Operation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OperationMapper {

    @Mapping(target = "type", constant = "DEBIT") // Default, logic usually handled in service
    Operation toEntity(VirementRequestDTO dto);

    @Mapping(target = "ribCompteSource", source = "compteSource.rib")
    @Mapping(target = "ribCompteDestination", source = "compteDestination.rib")
    OperationResponseDTO toDto(Operation entity);
}
