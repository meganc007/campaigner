package com.mcommings.campaigner.modules.people.mappers;

import com.mcommings.campaigner.modules.people.dtos.EventPlacePersonDTO;
import com.mcommings.campaigner.modules.people.entities.EventPlacePerson;
import org.mapstruct.Mapper;

@Mapper
public interface EventPlacePersonMapper {
    EventPlacePerson mapFromEventPlacePersonDto(EventPlacePersonDTO dto);

    EventPlacePersonDTO mapToEventPlacePersonDto(EventPlacePerson eventPlacePerson);
}
