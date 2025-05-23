package com.mcommings.campaigner.modules.people.mappers;

import com.mcommings.campaigner.modules.people.dtos.RaceDTO;
import com.mcommings.campaigner.modules.people.entities.Race;
import org.mapstruct.Mapper;

@Mapper
public interface RaceMapper {
    Race mapFromRaceDto(RaceDTO dto);

    RaceDTO mapToRaceDto(Race race);
}
