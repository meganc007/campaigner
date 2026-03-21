package com.mcommings.campaigner.modules.calendar.mappers;

import com.mcommings.campaigner.config.GlobalMapperConfig;
import com.mcommings.campaigner.modules.calendar.dtos.celestial_events.CreateCelestialEventDTO;
import com.mcommings.campaigner.modules.calendar.dtos.celestial_events.UpdateCelestialEventDTO;
import com.mcommings.campaigner.modules.calendar.dtos.celestial_events.ViewCelestialEventDTO;
import com.mcommings.campaigner.modules.calendar.entities.CelestialEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = GlobalMapperConfig.class)
public interface CelestialEventMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "campaign", ignore = true)
    CelestialEvent toEntity(CreateCelestialEventDTO dto);

    @Mapping(source = "campaign.uuid", target = "campaignUuid")
    @Mapping(source = "moon.id", target = "moonId")
    @Mapping(source = "sun.id", target = "sunId")
    @Mapping(source = "month.id", target = "monthId")
    @Mapping(source = "week.id", target = "weekId")
    @Mapping(source = "day.id", target = "dayId")
    ViewCelestialEventDTO toDto(CelestialEvent celestialEvent);

    void updateCelestialEventFromDto(
            UpdateCelestialEventDTO dto,
            @MappingTarget CelestialEvent entity
    );
}
