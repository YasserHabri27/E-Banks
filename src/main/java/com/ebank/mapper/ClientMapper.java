package com.ebank.mapper;

import com.ebank.dto.client.ClientMinimalDTO;
import com.ebank.dto.client.ClientRequestDTO;
import com.ebank.dto.client.ClientResponseDTO;
import com.ebank.entity.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClientMapper {

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", constant = "CLIENT")
    @Mapping(target = "enabled", constant = "true")
    Client toEntity(ClientRequestDTO dto);

    ClientResponseDTO toDto(Client entity);

    @Mapping(target = "nomComplet", expression = "java(entity.getNom() + \" \" + entity.getPrenom())")
    ClientMinimalDTO toMinimalDto(Client entity);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(ClientRequestDTO dto, @MappingTarget Client entity);
}
