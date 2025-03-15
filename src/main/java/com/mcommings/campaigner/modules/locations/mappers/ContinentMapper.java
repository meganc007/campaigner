package com.mcommings.campaigner.modules.locations.mappers;

import com.mcommings.campaigner.modules.locations.dtos.ContinentDTO;
import com.mcommings.campaigner.modules.locations.entities.Continent;
import org.mapstruct.Mapper;

@Mapper
public interface ContinentMapper {
    Continent mapFromContinentDto(ContinentDTO dto);

    ContinentDTO mapToContinentDto(Continent continent);
}

