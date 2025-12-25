package com.ebank.mapper;

import com.ebank.dto.compte.CompteBancaireMinimalDTO;
import com.ebank.dto.compte.CompteRequestDTO;
import com.ebank.dto.compte.CompteResponseDTO;
import com.ebank.entity.CompteBancaire;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { ClientMapper.class })
public interface CompteBancaireMapper {

    @Mapping(target = "statut", constant = "OUVERT")
    @Mapping(target = "solde", expression = "java(java.math.BigDecimal.ZERO)")
    CompteBancaire toEntity(CompteRequestDTO dto);

    CompteResponseDTO toDto(CompteBancaire entity);

    CompteBancaireMinimalDTO toMinimalDto(CompteBancaire entity);
}
