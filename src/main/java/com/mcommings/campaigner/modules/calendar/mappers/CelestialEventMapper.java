package com.mcommings.campaigner.modules.calendar.mappers;

import com.mcommings.campaigner.modules.calendar.dtos.CelestialEventDTO;
import com.mcommings.campaigner.modules.calendar.entities.CelestialEvent;
import org.mapstruct.Mapper;

@Mapper
public interface CelestialEventMapper {
    CelestialEvent mapFromCelestialEventDto(CelestialEventDTO dto);

    CelestialEventDTO mapToCelestialEventDto(CelestialEvent celestialEvent);
}
