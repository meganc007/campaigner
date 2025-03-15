package com.mcommings.campaigner.locations.mappers;

import com.mcommings.campaigner.locations.dtos.CountryDTO;
import com.mcommings.campaigner.locations.entities.Country;
import org.mapstruct.Mapper;

@Mapper
public interface CountryMapper {
    Country mapFromCountryDto(CountryDTO dto);

    CountryDTO mapToCountryDto(Country country);
}
