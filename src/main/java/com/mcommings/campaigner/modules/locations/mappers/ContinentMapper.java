package com.mcommings.campaigner.modules.locations.mappers;

import com.mcommings.campaigner.config.GlobalMapperConfig;
import com.mcommings.campaigner.modules.locations.dtos.continents.CreateContinentDTO;
import com.mcommings.campaigner.modules.locations.dtos.continents.UpdateContinentDTO;
import com.mcommings.campaigner.modules.locations.dtos.continents.ViewContinentDTO;
import com.mcommings.campaigner.modules.locations.entities.Continent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = GlobalMapperConfig.class)
public interface ContinentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "campaign", ignore = true)
    Continent toEntity(CreateContinentDTO dto);

    @Mapping(source = "campaign.uuid", target = "campaignUuid")
    ViewContinentDTO toDto(Continent continent);

    void updateContinentFromDto(
            UpdateContinentDTO dto,
            @MappingTarget Continent entity
    );
}

