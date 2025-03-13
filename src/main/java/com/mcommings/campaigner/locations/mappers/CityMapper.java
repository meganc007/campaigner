package com.mcommings.campaigner.locations.mappers;

import com.mcommings.campaigner.locations.dtos.CityDTO;
import com.mcommings.campaigner.locations.entities.City;
import org.mapstruct.Mapper;

@Mapper
public interface CityMapper {
    City mapFromCityDto(CityDTO dto);

    CityDTO mapToCityDto(City city);
}
