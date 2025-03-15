package com.mcommings.campaigner.modules.calendar.mappers;

import com.mcommings.campaigner.modules.calendar.dtos.MonthDTO;
import com.mcommings.campaigner.modules.calendar.entities.Month;
import org.mapstruct.Mapper;

@Mapper
public interface MonthMapper {
    Month mapFromMonthDto(MonthDTO dto);

    MonthDTO mapToMonthDto(Month month);
}
