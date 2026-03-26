package com.mcommings.campaigner.modules.calendar.mappers;

import com.mcommings.campaigner.config.GlobalMapperConfig;
import com.mcommings.campaigner.modules.calendar.dtos.months.CreateMonthDTO;
import com.mcommings.campaigner.modules.calendar.dtos.months.UpdateMonthDTO;
import com.mcommings.campaigner.modules.calendar.dtos.months.ViewMonthDTO;
import com.mcommings.campaigner.modules.calendar.entities.Month;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = GlobalMapperConfig.class)
public interface MonthMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "campaign", ignore = true)
    Month toEntity(CreateMonthDTO dto);

    @Mapping(source = "campaign.uuid", target = "campaignUuid")
    ViewMonthDTO toDto(Month month);

    void updateMonthFromDto(
            UpdateMonthDTO dto,
            @MappingTarget Month entity
    );
}
