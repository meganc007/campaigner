package com.mcommings.campaigner.mappers;

import com.mcommings.campaigner.dtos.ContinentDTO;
import com.mcommings.campaigner.entities.locations.Continent;
import org.mapstruct.Mapper;

@Mapper
public interface ContinentMapper {
    Continent continentDtotoContinent(ContinentDTO dto);

    ContinentDTO continentToContinentDto(Continent continent);
}
