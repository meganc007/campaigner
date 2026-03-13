package com.mcommings.campaigner.setup.locations.factories;

import com.mcommings.campaigner.modules.locations.dtos.cities.CreateCityDTO;
import com.mcommings.campaigner.modules.locations.dtos.cities.UpdateCityDTO;
import com.mcommings.campaigner.modules.locations.dtos.cities.ViewCityDTO;
import com.mcommings.campaigner.modules.locations.dtos.continents.CreateContinentDTO;
import com.mcommings.campaigner.modules.locations.dtos.continents.UpdateContinentDTO;
import com.mcommings.campaigner.modules.locations.dtos.continents.ViewContinentDTO;
import com.mcommings.campaigner.modules.locations.dtos.countries.CreateCountryDTO;
import com.mcommings.campaigner.modules.locations.dtos.countries.UpdateCountryDTO;
import com.mcommings.campaigner.modules.locations.dtos.countries.ViewCountryDTO;
import com.mcommings.campaigner.modules.locations.dtos.landmarks.CreateLandmarkDTO;
import com.mcommings.campaigner.modules.locations.dtos.landmarks.UpdateLandmarkDTO;
import com.mcommings.campaigner.modules.locations.dtos.landmarks.ViewLandmarkDTO;
import com.mcommings.campaigner.modules.locations.dtos.place_types.CreatePlaceTypeDTO;
import com.mcommings.campaigner.modules.locations.dtos.place_types.UpdatePlaceTypeDTO;
import com.mcommings.campaigner.modules.locations.dtos.place_types.ViewPlaceTypeDTO;
import com.mcommings.campaigner.modules.locations.dtos.regions.CreateRegionDTO;
import com.mcommings.campaigner.modules.locations.dtos.regions.UpdateRegionDTO;
import com.mcommings.campaigner.modules.locations.dtos.regions.ViewRegionDTO;
import com.mcommings.campaigner.modules.locations.dtos.settlement_types.CreateSettlementTypeDTO;
import com.mcommings.campaigner.modules.locations.dtos.settlement_types.UpdateSettlementTypeDTO;
import com.mcommings.campaigner.modules.locations.dtos.settlement_types.ViewSettlementTypeDTO;
import com.mcommings.campaigner.modules.locations.dtos.terrains.CreateTerrainDTO;
import com.mcommings.campaigner.modules.locations.dtos.terrains.UpdateTerrainDTO;
import com.mcommings.campaigner.modules.locations.dtos.terrains.ViewTerrainDTO;
import com.mcommings.campaigner.modules.locations.entities.*;
import com.mcommings.campaigner.setup.locations.builders.*;
import com.mcommings.campaigner.setup.locations.fixtures.LocationsTestConstants;

public class LocationsTestDataFactory {

    //CONTINENTS
    public static Continent continent() {
        return ContinentBuilder.aContinent().build();
    }

    public static ViewContinentDTO viewContinentDTO() {
        ViewContinentDTO dto = new ViewContinentDTO();
        dto.setId(LocationsTestConstants.CONTINENT_ID);
        dto.setName(LocationsTestConstants.CONTINENT_NAME);
        dto.setDescription(LocationsTestConstants.CONTINENT_DESCRIPTION);
        dto.setCampaignUuid(LocationsTestConstants.CAMPAIGN_UUID);
        return dto;
    }

    public static CreateContinentDTO createContinentDTO() {
        CreateContinentDTO dto = new CreateContinentDTO();
        dto.setName(LocationsTestConstants.CONTINENT_NAME);
        dto.setDescription(LocationsTestConstants.CONTINENT_DESCRIPTION);
        dto.setCampaignUuid(LocationsTestConstants.CAMPAIGN_UUID);
        return dto;
    }

    public static UpdateContinentDTO updateContinentDTO() {
        UpdateContinentDTO dto = new UpdateContinentDTO();
        dto.setId(LocationsTestConstants.CONTINENT_ID);
        dto.setName(LocationsTestConstants.CONTINENT_NAME);
        dto.setDescription(LocationsTestConstants.CONTINENT_DESCRIPTION);
        dto.setCampaignUuid(LocationsTestConstants.CAMPAIGN_UUID);
        return dto;
    }

    //COUNTRIES

    public static Country country() {
        return CountryBuilder.aCountry().build();
    }

    public static ViewCountryDTO viewCountryDTO() {
        ViewCountryDTO dto = new ViewCountryDTO();
        dto.setId(LocationsTestConstants.COUNTRY_ID);
        dto.setName(LocationsTestConstants.COUNTRY_NAME);
        dto.setDescription(LocationsTestConstants.COUNTRY_DESCRIPTION);
        dto.setCampaignUuid(LocationsTestConstants.CAMPAIGN_UUID);
        dto.setContinentId(LocationsTestConstants.CONTINENT_ID);
        dto.setGovernmentId(LocationsTestConstants.GOVERNMENT_ID);
        return dto;
    }

    public static CreateCountryDTO createCountryDTO() {
        CreateCountryDTO dto = new CreateCountryDTO();
        dto.setName(LocationsTestConstants.COUNTRY_NAME);
        dto.setDescription(LocationsTestConstants.COUNTRY_DESCRIPTION);
        dto.setCampaignUuid(LocationsTestConstants.CAMPAIGN_UUID);
        dto.setContinentId(LocationsTestConstants.CONTINENT_ID);
        dto.setGovernmentId(LocationsTestConstants.GOVERNMENT_ID);
        return dto;
    }

    public static UpdateCountryDTO updateCountryDTO() {
        UpdateCountryDTO dto = new UpdateCountryDTO();
        dto.setId(LocationsTestConstants.COUNTRY_ID);
        dto.setName(LocationsTestConstants.COUNTRY_NAME);
        dto.setDescription(LocationsTestConstants.COUNTRY_DESCRIPTION);
        dto.setCampaignUuid(LocationsTestConstants.CAMPAIGN_UUID);
        return dto;
    }

    //CITIES

    public static City city() {
        return CityBuilder.aCity().build();
    }

    public static ViewCityDTO viewCityDTO() {
        ViewCityDTO dto = new ViewCityDTO();
        dto.setId(LocationsTestConstants.CITY_ID);
        dto.setName(LocationsTestConstants.CITY_NAME);
        dto.setDescription(LocationsTestConstants.CITY_DESCRIPTION);
        dto.setCampaignUuid(LocationsTestConstants.CAMPAIGN_UUID);
        dto.setWealthId(LocationsTestConstants.WEALTH_ID);
        dto.setCountryId(LocationsTestConstants.COUNTRY_ID);
        dto.setSettlementTypeId(LocationsTestConstants.SETTLEMENTTYPE_ID);
        dto.setGovernmentId(LocationsTestConstants.GOVERNMENT_ID);
        dto.setRegionId(LocationsTestConstants.REGION_ID);
        return dto;
    }

    public static CreateCityDTO createCityDTO() {
        CreateCityDTO dto = new CreateCityDTO();
        dto.setName(LocationsTestConstants.CITY_NAME);
        dto.setDescription(LocationsTestConstants.CITY_DESCRIPTION);
        dto.setCampaignUuid(LocationsTestConstants.CAMPAIGN_UUID);
        dto.setWealthId(LocationsTestConstants.WEALTH_ID);
        dto.setCountryId(LocationsTestConstants.COUNTRY_ID);
        dto.setSettlementTypeId(LocationsTestConstants.SETTLEMENTTYPE_ID);
        dto.setGovernmentId(LocationsTestConstants.GOVERNMENT_ID);
        dto.setRegionId(LocationsTestConstants.REGION_ID);
        return dto;
    }

    public static UpdateCityDTO updateCityDTO() {
        UpdateCityDTO dto = new UpdateCityDTO();
        dto.setId(LocationsTestConstants.CITY_ID);
        dto.setName(LocationsTestConstants.CITY_NAME);
        dto.setDescription(LocationsTestConstants.CITY_DESCRIPTION);
        dto.setCampaignUuid(LocationsTestConstants.CAMPAIGN_UUID);
        return dto;
    }

    //LANDMARKS

    public static Landmark landmark() {
        return LandmarkBuilder.aLandmark().build();
    }

    public static ViewLandmarkDTO viewLandmarkDTO() {
        ViewLandmarkDTO dto = new ViewLandmarkDTO();
        dto.setId(LocationsTestConstants.LANDMARK_ID);
        dto.setName(LocationsTestConstants.LANDMARK_NAME);
        dto.setDescription(LocationsTestConstants.LANDMARK_DESCRIPTION);
        dto.setCampaignUuid(LocationsTestConstants.CAMPAIGN_UUID);
        dto.setRegionId(LocationsTestConstants.REGION_ID);
        return dto;
    }

    public static CreateLandmarkDTO createLandmarkDTO() {
        CreateLandmarkDTO dto = new CreateLandmarkDTO();
        dto.setName(LocationsTestConstants.LANDMARK_NAME);
        dto.setDescription(LocationsTestConstants.LANDMARK_DESCRIPTION);
        dto.setCampaignUuid(LocationsTestConstants.CAMPAIGN_UUID);
        dto.setRegionId(LocationsTestConstants.REGION_ID);
        return dto;
    }

    public static UpdateLandmarkDTO updateLandmarkDTO() {
        UpdateLandmarkDTO dto = new UpdateLandmarkDTO();
        dto.setId(LocationsTestConstants.LANDMARK_ID);
        dto.setName(LocationsTestConstants.LANDMARK_NAME);
        dto.setDescription(LocationsTestConstants.LANDMARK_DESCRIPTION);
        dto.setCampaignUuid(LocationsTestConstants.CAMPAIGN_UUID);
        return dto;
    }

    //SETTLEMENT TYPES

    public static SettlementType settlementType() {
        return SettlementTypeBuilder.aSettlementType().build();
    }

    public static ViewSettlementTypeDTO viewSettlementTypeDTO() {
        ViewSettlementTypeDTO dto = new ViewSettlementTypeDTO();
        dto.setId(LocationsTestConstants.SETTLEMENTTYPE_ID);
        dto.setName(LocationsTestConstants.SETTLEMENTTYPE_NAME);
        dto.setDescription(LocationsTestConstants.SETTLEMENTTYPE_DESCRIPTION);
        return dto;
    }

    public static CreateSettlementTypeDTO createSettlementTypeDTO() {
        CreateSettlementTypeDTO dto = new CreateSettlementTypeDTO();
        dto.setName(LocationsTestConstants.SETTLEMENTTYPE_NAME);
        dto.setDescription(LocationsTestConstants.SETTLEMENTTYPE_DESCRIPTION);
        return dto;
    }

    public static UpdateSettlementTypeDTO updateSettlementTypeDTO() {
        UpdateSettlementTypeDTO dto = new UpdateSettlementTypeDTO();
        dto.setId(LocationsTestConstants.SETTLEMENTTYPE_ID);
        dto.setName(LocationsTestConstants.SETTLEMENTTYPE_NAME);
        dto.setDescription(LocationsTestConstants.SETTLEMENTTYPE_DESCRIPTION);
        return dto;
    }

    //PLACE TYPES

    public static PlaceType placeType() {
        return PlaceTypeBuilder.aPlaceType().build();
    }

    public static ViewPlaceTypeDTO viewPlaceTypeDTO() {
        ViewPlaceTypeDTO dto = new ViewPlaceTypeDTO();
        dto.setId(LocationsTestConstants.PLACETYPE_ID);
        dto.setName(LocationsTestConstants.PLACETYPE_NAME);
        dto.setDescription(LocationsTestConstants.PLACETYPE_DESCRIPTION);
        return dto;
    }

    public static CreatePlaceTypeDTO createPlaceTypeDTO() {
        CreatePlaceTypeDTO dto = new CreatePlaceTypeDTO();
        dto.setName(LocationsTestConstants.PLACETYPE_NAME);
        dto.setDescription(LocationsTestConstants.PLACETYPE_DESCRIPTION);
        return dto;
    }

    public static UpdatePlaceTypeDTO updatePlaceTypeDTO() {
        UpdatePlaceTypeDTO dto = new UpdatePlaceTypeDTO();
        dto.setId(LocationsTestConstants.PLACETYPE_ID);
        dto.setName(LocationsTestConstants.PLACETYPE_NAME);
        dto.setDescription(LocationsTestConstants.PLACETYPE_DESCRIPTION);
        return dto;
    }

    //TERRAIN

    public static Terrain terrain() {
        return TerrainBuilder.aTerrain().build();
    }

    public static ViewTerrainDTO viewTerrainDTO() {
        ViewTerrainDTO dto = new ViewTerrainDTO();
        dto.setId(LocationsTestConstants.TERRAIN_ID);
        dto.setName(LocationsTestConstants.TERRAIN_NAME);
        dto.setDescription(LocationsTestConstants.TERRAIN_DESCRIPTION);
        return dto;
    }

    public static CreateTerrainDTO createTerrainDTO() {
        CreateTerrainDTO dto = new CreateTerrainDTO();
        dto.setName(LocationsTestConstants.TERRAIN_NAME);
        dto.setDescription(LocationsTestConstants.TERRAIN_DESCRIPTION);
        return dto;
    }

    public static UpdateTerrainDTO updateTerrainDTO() {
        UpdateTerrainDTO dto = new UpdateTerrainDTO();
        dto.setId(LocationsTestConstants.TERRAIN_ID);
        dto.setName(LocationsTestConstants.TERRAIN_NAME);
        dto.setDescription(LocationsTestConstants.TERRAIN_DESCRIPTION);
        return dto;
    }

    //REGION

    public static Region region() {
        return RegionBuilder.aRegion().build();
    }

    public static ViewRegionDTO viewRegionDTO() {
        ViewRegionDTO dto = new ViewRegionDTO();
        dto.setId(LocationsTestConstants.REGION_ID);
        dto.setName(LocationsTestConstants.REGION_NAME);
        dto.setDescription(LocationsTestConstants.REGION_DESCRIPTION);
        dto.setCampaignUuid(LocationsTestConstants.CAMPAIGN_UUID);
        dto.setClimateId(LocationsTestConstants.CLIMATE_ID);
        dto.setCountryId(LocationsTestConstants.COUNTRY_ID);
        return dto;
    }

    public static CreateRegionDTO createRegionDTO() {
        CreateRegionDTO dto = new CreateRegionDTO();
        dto.setName(LocationsTestConstants.REGION_NAME);
        dto.setDescription(LocationsTestConstants.REGION_DESCRIPTION);
        dto.setCampaignUuid(LocationsTestConstants.CAMPAIGN_UUID);
        dto.setClimateId(LocationsTestConstants.CLIMATE_ID);
        dto.setCountryId(LocationsTestConstants.COUNTRY_ID);
        return dto;
    }

    public static UpdateRegionDTO updateRegionDTO() {
        UpdateRegionDTO dto = new UpdateRegionDTO();
        dto.setId(LocationsTestConstants.REGION_ID);
        dto.setName(LocationsTestConstants.REGION_NAME);
        dto.setDescription(LocationsTestConstants.REGION_DESCRIPTION);
        dto.setCampaignUuid(LocationsTestConstants.CAMPAIGN_UUID);
        return dto;
    }

}