package com.mcommings.campaigner.modules.calendar.mappers;

import com.mcommings.campaigner.modules.calendar.dtos.weeks.CreateWeekDTO;
import com.mcommings.campaigner.modules.calendar.dtos.weeks.UpdateWeekDTO;
import com.mcommings.campaigner.modules.calendar.dtos.weeks.ViewWeekDTO;
import com.mcommings.campaigner.modules.calendar.entities.Week;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper
public interface WeekMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "campaign", ignore = true)
    Week toEntity(CreateWeekDTO dto);

    @Mapping(source = "campaign.uuid", target = "campaignUuid")
    @Mapping(source = "month.id", target = "monthId")
    ViewWeekDTO toDto(Week week);

    void updateWeekFromDto(
            UpdateWeekDTO dto,
            @MappingTarget Week entity
    );
}
