package com.mcommings.campaigner.modules.people.mappers;

import com.mcommings.campaigner.modules.people.dtos.PersonDTO;
import com.mcommings.campaigner.modules.people.entities.Person;
import org.mapstruct.Mapper;

@Mapper
public interface PersonMapper {
    Person mapFromPersonDto(PersonDTO dto);

    PersonDTO mapToPersonDto(Person person);
}
