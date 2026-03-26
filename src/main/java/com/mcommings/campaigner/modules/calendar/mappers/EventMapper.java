package com.mcommings.campaigner.modules.calendar.mappers;

import com.mcommings.campaigner.config.GlobalMapperConfig;
import com.mcommings.campaigner.modules.calendar.dtos.events.CreateEventDTO;
import com.mcommings.campaigner.modules.calendar.dtos.events.UpdateEventDTO;
import com.mcommings.campaigner.modules.calendar.dtos.events.ViewEventDTO;
import com.mcommings.campaigner.modules.calendar.entities.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = GlobalMapperConfig.class)
public interface EventMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "campaign", ignore = true)
    Event toEntity(CreateEventDTO dto);

    @Mapping(source = "campaign.uuid", target = "campaignUuid")
    @Mapping(source = "month.id", target = "monthId")
    @Mapping(source = "week.id", target = "weekId")
    @Mapping(source = "day.id", target = "dayId")
    @Mapping(source = "continent.id", target = "continentId")
    @Mapping(source = "country.id", target = "countryId")
    @Mapping(source = "city.id", target = "cityId")
    ViewEventDTO toDto(Event event);

    void updateEventFromDto(
            UpdateEventDTO dto,
            @MappingTarget Event entity
    );
}
