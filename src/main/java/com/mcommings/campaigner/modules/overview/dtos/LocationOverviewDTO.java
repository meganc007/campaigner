package com.mcommings.campaigner.modules.overview.dtos;

import com.mcommings.campaigner.modules.common.dtos.ClimateDTO;
import com.mcommings.campaigner.modules.locations.dtos.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationOverviewDTO {
    private List<ContinentDTO> continents;
    private List<CountryDTO> countries;
    private List<CityDTO> cities;
    private List<RegionDTO> regions;
    private List<LandmarkDTO> landmarks;
    private List<PlaceDTO> places;
    private List<PlaceTypeDTO> placeTypes;
    private List<TerrainDTO> terrains;
    private List<ClimateDTO> climates;
}
