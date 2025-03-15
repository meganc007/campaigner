package com.mcommings.campaigner.modules.locations.mappers;

import com.mcommings.campaigner.modules.locations.dtos.CountryDTO;
import com.mcommings.campaigner.modules.locations.entities.Country;
import org.mapstruct.Mapper;

@Mapper
public interface CountryMapper {
    Country mapFromCountryDto(CountryDTO dto);

    CountryDTO mapToCountryDto(Country country);
}
