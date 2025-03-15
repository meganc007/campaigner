package com.mcommings.campaigner.modules.calendar.mappers;

import com.mcommings.campaigner.modules.calendar.dtos.MoonDTO;
import com.mcommings.campaigner.modules.calendar.entities.Moon;
import org.mapstruct.Mapper;

@Mapper
public interface MoonMapper {
    Moon mapFromMoonDto(MoonDTO dto);

    MoonDTO mapToMoonDto(Moon moon);
}
