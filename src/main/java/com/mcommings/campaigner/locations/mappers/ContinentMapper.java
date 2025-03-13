package com.mcommings.campaigner.locations.mappers;

import com.mcommings.campaigner.locations.dtos.ContinentDTO;
import com.mcommings.campaigner.locations.entities.Continent;
import org.mapstruct.Mapper;

@Mapper
public interface ContinentMapper {
    Continent mapFromContinentDto(ContinentDTO dto);

    ContinentDTO mapToContinentDto(Continent continent);
}

