package com.mcommings.campaigner.modules.calendar.mappers;

import com.mcommings.campaigner.modules.calendar.dtos.SunDTO;
import com.mcommings.campaigner.modules.calendar.entities.Sun;
import org.mapstruct.Mapper;

@Mapper
public interface SunMapper {
    Sun mapFromSunDto(SunDTO dto);

    SunDTO mapToSunDto(Sun sun);
}
