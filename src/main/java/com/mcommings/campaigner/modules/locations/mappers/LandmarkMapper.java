package com.mcommings.campaigner.modules.locations.mappers;

import com.mcommings.campaigner.modules.locations.dtos.LandmarkDTO;
import com.mcommings.campaigner.modules.locations.entities.Landmark;
import org.mapstruct.Mapper;

@Mapper
public interface LandmarkMapper {
    Landmark mapFromLandmarkDto(LandmarkDTO dto);

    LandmarkDTO mapToLandmarkDto(Landmark landmark);
}
