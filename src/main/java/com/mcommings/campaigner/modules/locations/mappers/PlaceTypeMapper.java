package com.mcommings.campaigner.modules.locations.mappers;

import com.mcommings.campaigner.config.GlobalMapperConfig;
import com.mcommings.campaigner.modules.locations.dtos.place_types.CreatePlaceTypeDTO;
import com.mcommings.campaigner.modules.locations.dtos.place_types.UpdatePlaceTypeDTO;
import com.mcommings.campaigner.modules.locations.dtos.place_types.ViewPlaceTypeDTO;
import com.mcommings.campaigner.modules.locations.entities.PlaceType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = GlobalMapperConfig.class)
public interface PlaceTypeMapper {

    @Mapping(target = "id", ignore = true)
    PlaceType toEntity(CreatePlaceTypeDTO dto);

    ViewPlaceTypeDTO toDto(PlaceType placeType);

    void updatePlaceTypeFromDto(
            UpdatePlaceTypeDTO dto,
            @MappingTarget PlaceType entity
    );
}
