package com.mcommings.campaigner.modules.calendar.mappers;

import com.mcommings.campaigner.config.GlobalMapperConfig;
import com.mcommings.campaigner.modules.calendar.dtos.moons.CreateMoonDTO;
import com.mcommings.campaigner.modules.calendar.dtos.moons.UpdateMoonDTO;
import com.mcommings.campaigner.modules.calendar.dtos.moons.ViewMoonDTO;
import com.mcommings.campaigner.modules.calendar.entities.Moon;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = GlobalMapperConfig.class)
public interface MoonMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "campaign", ignore = true)
    Moon toEntity(CreateMoonDTO dto);

    @Mapping(source = "campaign.uuid", target = "campaignUuid")
    ViewMoonDTO toDto(Moon moon);

    void updateMoonFromDto(
            UpdateMoonDTO dto,
            @MappingTarget Moon entity
    );
}
