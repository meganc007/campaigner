package com.mcommings.campaigner.modules.locations.mappers;

import com.mcommings.campaigner.modules.locations.dtos.PlaceTypeDTO;
import com.mcommings.campaigner.modules.locations.entities.PlaceType;
import org.mapstruct.Mapper;

@Mapper
public interface PlaceTypeMapper {

    PlaceType mapFromPlaceTypeDto(PlaceTypeDTO dto);

    PlaceTypeDTO mapToPlaceTypeDto(PlaceType placeType);
}
