package com.mcommings.campaigner.modules.overview.services;

import com.mcommings.campaigner.modules.overview.dtos.LocationOverviewDTO;
import com.mcommings.campaigner.modules.overview.facades.mappers.LocationMapperFacade;
import com.mcommings.campaigner.modules.overview.facades.repositories.LocationRepositoryFacade;
import com.mcommings.campaigner.modules.overview.services.interfaces.ILocationOverview;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LocationOverviewService implements ILocationOverview {

    private final LocationMapperFacade locationMapperFacade;
    private final LocationRepositoryFacade locationRepositoryFacade;

    @Override
    public LocationOverviewDTO getLocationOverview(UUID campaignId) {
        var continents = locationRepositoryFacade.findContinentsByCampaign(campaignId)
                .stream().map(locationMapperFacade::toContinentDto).toList();

        var countries = locationRepositoryFacade.findCountriesByCampaign(campaignId)
                .stream().map(locationMapperFacade::toCountryDto).toList();

        var cities = locationRepositoryFacade.findCitiesByCampaign(campaignId)
                .stream().map(locationMapperFacade::toCityDto).toList();

        var regions = locationRepositoryFacade.findRegionsByCampaign(campaignId)
                .stream().map(locationMapperFacade::toRegionDto).toList();

        var landmarks = locationRepositoryFacade.findLandmarksByCampaign(campaignId)
                .stream().map(locationMapperFacade::toLandmarkDto).toList();

        var places = locationRepositoryFacade.findPlacesByCampaign(campaignId)
                .stream().map(locationMapperFacade::toPlaceDto).toList();

        var placeTypes = locationRepositoryFacade.getPlaceTypesForCampaign(campaignId)
                .stream().map(locationMapperFacade::toPlaceTypeDto).toList();

        var terrains = locationRepositoryFacade.getTerrainsForCampaign(campaignId)
                .stream().map(locationMapperFacade::toTerrainDto).toList();

        var climates = locationRepositoryFacade.getClimatesForCampaign(campaignId)
                .stream().map(locationMapperFacade::toClimateDto).toList();

        return LocationOverviewDTO.builder()
                .continents(continents)
                .countries(countries)
                .cities(cities)
                .regions(regions)
                .landmarks(landmarks)
                .places(places)
                .placeTypes(placeTypes)
                .terrains(terrains)
                .climates(climates)
                .build();
    }

}
