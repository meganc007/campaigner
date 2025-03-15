package com.mcommings.campaigner.modules.calendar.mappers;

import com.mcommings.campaigner.modules.calendar.dtos.WeekDTO;
import com.mcommings.campaigner.modules.calendar.entities.Week;
import org.mapstruct.Mapper;

@Mapper
public interface WeekMapper {
    Week mapFromWeekDto(WeekDTO dto);

    WeekDTO mapToWeekDto(Week week);
}
