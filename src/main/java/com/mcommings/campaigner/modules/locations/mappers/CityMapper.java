package com.mcommings.campaigner.modules.locations.mappers;

import com.mcommings.campaigner.modules.locations.dtos.CityDTO;
import com.mcommings.campaigner.modules.locations.entities.City;
import org.mapstruct.Mapper;

@Mapper
public interface CityMapper {
    City mapFromCityDto(CityDTO dto);

    CityDTO mapToCityDto(City city);
}
