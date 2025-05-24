package com.mcommings.campaigner.modules.overview.facades.repositories;

import com.mcommings.campaigner.modules.common.entities.Climate;
import com.mcommings.campaigner.modules.common.repositories.IClimateRepository;
import com.mcommings.campaigner.modules.locations.entities.*;
import com.mcommings.campaigner.modules.locations.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class LocationRepositoryFacade {

    private final IContinentRepository continentRepository;
    private final ICountryRepository countryRepository;
    private final ICityRepository cityRepository;
    private final IRegionRepository regionRepository;
    private final ILandmarkRepository landmarkRepository;
    private final IPlaceRepository placeRepository;
    private final IPlaceTypesRepository placeTypeRepository;
    private final ITerrainRepository terrainRepository;
    private final IClimateRepository climateRepository;

    public List<Continent> findContinentsByCampaign(UUID uuid) {
        return continentRepository.findByfk_campaign_uuid(uuid);
    }

    public List<Country> findCountriesByCampaign(UUID uuid) {
        return countryRepository.findByfk_campaign_uuid(uuid);
    }

    public List<City> findCitiesByCampaign(UUID uuid) {
        return cityRepository.findByfk_campaign_uuid(uuid);
    }

    public List<Region> findRegionsByCampaign(UUID uuid) {
        return regionRepository.findByfk_campaign_uuid(uuid);
    }

    public List<Landmark> findLandmarksByCampaign(UUID uuid) {
        return landmarkRepository.findByfk_campaign_uuid(uuid);
    }

    public List<Place> findPlacesByCampaign(UUID uuid) {
        return placeRepository.findByfk_campaign_uuid(uuid);
    }

    public List<PlaceType> getPlaceTypesForCampaign(UUID uuid) {
        List<Integer> placeTypeIds = placeRepository.findPlaceTypeIdsByCampaign(uuid);
        if (placeTypeIds.isEmpty()) {
            return Collections.emptyList();
        }
        return placeTypeRepository.findByIdIn(placeTypeIds);
    }

    public List<Terrain> getTerrainsForCampaign(UUID uuid) {
        List<Integer> terrainIds = placeRepository.findTerrainIdsByCampaign(uuid);
        if (terrainIds.isEmpty()) {
            return Collections.emptyList();
        }
        return terrainRepository.findByIdIn(terrainIds);
    }

    public List<Climate> getClimatesForCampaign(UUID uuid) {
        List<Integer> climateIds = regionRepository.findClimateIdsByCampaign(uuid);
        if (climateIds.isEmpty()) {
            return Collections.emptyList();
        }
        return climateRepository.findByIdIn(climateIds);
    }

}
