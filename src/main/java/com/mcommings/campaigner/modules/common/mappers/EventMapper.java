package com.mcommings.campaigner.modules.common.mappers;

import com.mcommings.campaigner.modules.common.dtos.EventDTO;
import com.mcommings.campaigner.modules.common.entities.Event;
import org.mapstruct.Mapper;

@Mapper
public interface EventMapper {
    Event mapFromEventDto(EventDTO dto);

    EventDTO mapToEventDto(Event event);
}
