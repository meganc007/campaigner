package com.mcommings.campaigner.locations.mappers;

import com.mcommings.campaigner.locations.dtos.PlaceDTO;
import com.mcommings.campaigner.locations.entities.Place;
import org.mapstruct.Mapper;

@Mapper
public interface PlaceMapper {

    Place mapFromPlace(PlaceDTO dto);

    PlaceDTO mapToPlaceDto(Place place);
}
