package com.mcommings.campaigner.modules.people.mappers;

import com.mcommings.campaigner.modules.people.dtos.NamedMonsterDTO;
import com.mcommings.campaigner.modules.people.entities.NamedMonster;
import org.mapstruct.Mapper;

@Mapper
public interface NamedMonsterMapper {
    NamedMonster mapFromNamedMonsterDto(NamedMonsterDTO dto);

    NamedMonsterDTO mapToNamedMonsterDto(NamedMonster namedMonster);
}
