package com.mcommings.campaigner.modules.overview.facades.mappers;

import com.mcommings.campaigner.modules.common.dtos.ClimateDTO;
import com.mcommings.campaigner.modules.common.entities.Climate;
import com.mcommings.campaigner.modules.common.mappers.ClimateMapper;
import com.mcommings.campaigner.modules.locations.dtos.*;
import com.mcommings.campaigner.modules.locations.entities.*;
import com.mcommings.campaigner.modules.locations.mappers.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LocationMapperFacade {

    private final ContinentMapper continentMapper;
    private final CountryMapper countryMapper;
    private final CityMapper cityMapper;
    private final RegionMapper regionMapper;
    private final LandmarkMapper landmarkMapper;
    private final PlaceMapper placeMapper;
    private final PlaceTypeMapper placeTypeMapper;
    private final TerrainMapper terrainMapper;
    private final ClimateMapper climateMapper;

    public ContinentDTO toContinentDto(Continent entity) {
        return continentMapper.mapToContinentDto(entity);
    }

    public CountryDTO toCountryDto(Country entity) {
        return countryMapper.mapToCountryDto(entity);
    }

    public CityDTO toCityDto(City entity) {
        return cityMapper.mapToCityDto(entity);
    }

    public RegionDTO toRegionDto(Region entity) {
        return regionMapper.mapToRegionDto(entity);
    }

    public LandmarkDTO toLandmarkDto(Landmark entity) {
        return landmarkMapper.mapToLandmarkDto(entity);
    }

    public PlaceDTO toPlaceDto(Place entity) {
        return placeMapper.mapToPlaceDto(entity);
    }

    public PlaceTypeDTO toPlaceTypeDto(PlaceType entity) {
        return placeTypeMapper.mapToPlaceTypeDto(entity);
    }

    public TerrainDTO toTerrainDto(Terrain entity) {
        return terrainMapper.mapToTerrainDto(entity);
    }

    public ClimateDTO toClimateDto(Climate entity) {
        return climateMapper.mapToClimateDto(entity);
    }
}
