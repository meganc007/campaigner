package com.mcommings.campaigner.locations.mappers;

import com.mcommings.campaigner.locations.dtos.LandmarkDTO;
import com.mcommings.campaigner.locations.entities.Landmark;
import org.mapstruct.Mapper;

@Mapper
public interface LandmarkMapper {
    Landmark mapFromLandmarkDto(LandmarkDTO dto);

    LandmarkDTO mapToLandmarkDto(Landmark landmark);
}
