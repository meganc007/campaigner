package com.mcommings.campaigner.modules.people.mappers;

import com.mcommings.campaigner.modules.people.dtos.GenericMonsterDTO;
import com.mcommings.campaigner.modules.people.entities.GenericMonster;
import org.mapstruct.Mapper;

@Mapper
public interface GenericMonsterMapper {
    GenericMonster mapFromGenericMonsterDto(GenericMonsterDTO dto);

    GenericMonsterDTO mapToGenericMonsterDto(GenericMonster genericMonster);
}
