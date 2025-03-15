package com.mcommings.campaigner.modules.locations.mappers;

import com.mcommings.campaigner.modules.locations.dtos.PlaceDTO;
import com.mcommings.campaigner.modules.locations.entities.Place;
import org.mapstruct.Mapper;

@Mapper
public interface PlaceMapper {

    Place mapFromPlaceDto(PlaceDTO dto);

    PlaceDTO mapToPlaceDto(Place place);
}
