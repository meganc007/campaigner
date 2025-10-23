package com.mcommings.campaigner.modules.calendar.mappers;

import com.mcommings.campaigner.modules.calendar.dtos.EventDTO;
import com.mcommings.campaigner.modules.calendar.entities.Event;
import org.mapstruct.Mapper;

@Mapper
public interface EventMapper {
    Event mapFromEventDto(EventDTO dto);

    EventDTO mapToEventDto(Event event);
}
