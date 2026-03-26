package com.mcommings.campaigner.modules.calendar.mappers;

import com.mcommings.campaigner.config.GlobalMapperConfig;
import com.mcommings.campaigner.modules.calendar.dtos.suns.CreateSunDTO;
import com.mcommings.campaigner.modules.calendar.dtos.suns.UpdateSunDTO;
import com.mcommings.campaigner.modules.calendar.dtos.suns.ViewSunDTO;
import com.mcommings.campaigner.modules.calendar.entities.Sun;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = GlobalMapperConfig.class)
public interface SunMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "campaign", ignore = true)
    Sun toEntity(CreateSunDTO dto);

    @Mapping(source = "campaign.uuid", target = "campaignUuid")
    ViewSunDTO toDto(Sun sun);

    void updateSunFromDto(
            UpdateSunDTO dto,
            @MappingTarget Sun entity
    );
}
