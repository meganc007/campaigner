package com.mcommings.campaigner.modules.locations.mappers;

import com.mcommings.campaigner.config.GlobalMapperConfig;
import com.mcommings.campaigner.modules.locations.dtos.places.CreatePlaceDTO;
import com.mcommings.campaigner.modules.locations.dtos.places.UpdatePlaceDTO;
import com.mcommings.campaigner.modules.locations.dtos.places.ViewPlaceDTO;
import com.mcommings.campaigner.modules.locations.entities.Place;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = GlobalMapperConfig.class)
public interface PlaceMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "campaign", ignore = true)
    Place toEntity(CreatePlaceDTO dto);

    @Mapping(source = "campaign.uuid", target = "campaignUuid")
    @Mapping(source = "placeType.id", target = "placeTypeId")
    @Mapping(source = "terrain.id", target = "terrainId")
    @Mapping(source = "country.id", target = "countryId")
    @Mapping(source = "city.id", target = "cityId")
    @Mapping(source = "region.id", target = "regionId")
    ViewPlaceDTO toDto(Place place);

    void updatePlaceFromDto(
            UpdatePlaceDTO dto,
            @MappingTarget Place entity
    );
}
