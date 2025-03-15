package com.mcommings.campaigner.modules.calendar.mappers;

import com.mcommings.campaigner.modules.calendar.dtos.DayDTO;
import com.mcommings.campaigner.modules.calendar.entities.Day;
import org.mapstruct.Mapper;

@Mapper
public interface DayMapper {
    Day mapFromDayDto(DayDTO dto);

    DayDTO mapToDayDto(Day day);
}
