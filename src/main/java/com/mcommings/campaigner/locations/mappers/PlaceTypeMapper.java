package com.mcommings.campaigner.locations.mappers;

import com.mcommings.campaigner.locations.dtos.PlaceTypeDTO;
import com.mcommings.campaigner.locations.entities.PlaceType;
import org.mapstruct.Mapper;

@Mapper
public interface PlaceTypeMapper {

    PlaceType mapFromPlaceTypeDto(PlaceTypeDTO dto);

    PlaceTypeDTO mapToPlaceTypeDto(PlaceType placeType);
}
