package com.mcommings.campaigner.services.overview;

import com.mcommings.campaigner.modules.locations.dtos.ContinentDTO;
import com.mcommings.campaigner.modules.locations.entities.Continent;
import com.mcommings.campaigner.modules.overview.dtos.LocationOverviewDTO;
import com.mcommings.campaigner.modules.overview.facades.mappers.LocationMapperFacade;
import com.mcommings.campaigner.modules.overview.facades.repositories.LocationRepositoryFacade;
import com.mcommings.campaigner.modules.overview.services.LocationOverviewService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class LocationOverviewTest {

    @Mock
    private LocationRepositoryFacade locationRepositoryFacade;

    @Mock
    private LocationMapperFacade locationMapperFacade;

    @InjectMocks
    private LocationOverviewService locationOverviewService;

    @Test
    void whenCampaignUUIDIsValid_getLocationOverview_returnsAggregatedLocationData() {
        UUID campaignId = UUID.randomUUID();

        // Mock data
        Continent continent = new Continent(1, "Continentia", "desc", campaignId);
        ContinentDTO continentDTO = new ContinentDTO(1, "Continentia", "desc", campaignId);

        when(locationRepositoryFacade.findContinentsByCampaign(campaignId)).thenReturn(List.of(continent));
        when(locationMapperFacade.toContinentDto(continent)).thenReturn(continentDTO);

        LocationOverviewDTO result = locationOverviewService.getLocationOverview(campaignId);

        assertNotNull(result);
        assertEquals(1, result.getContinents().size());
        assertEquals("Continentia", result.getContinents().get(0).getName());

        verify(locationRepositoryFacade).findContinentsByCampaign(campaignId);
        verify(locationMapperFacade).toContinentDto(continent);
    }

    @Test
    void whenCampaignUUIDIsNotValid_getLocationOverview_returnsEmptyList() {
        UUID invalidCampaignId = UUID.randomUUID();

        // Mock repository methods to return empty lists
        when(locationRepositoryFacade.findContinentsByCampaign(invalidCampaignId)).thenReturn(Collections.emptyList());
        when(locationRepositoryFacade.findCountriesByCampaign(invalidCampaignId)).thenReturn(Collections.emptyList());
        when(locationRepositoryFacade.findCitiesByCampaign(invalidCampaignId)).thenReturn(Collections.emptyList());
        when(locationRepositoryFacade.findRegionsByCampaign(invalidCampaignId)).thenReturn(Collections.emptyList());
        when(locationRepositoryFacade.findLandmarksByCampaign(invalidCampaignId)).thenReturn(Collections.emptyList());
        when(locationRepositoryFacade.findPlacesByCampaign(invalidCampaignId)).thenReturn(Collections.emptyList());
        when(locationRepositoryFacade.getPlaceTypesForCampaign(invalidCampaignId)).thenReturn(Collections.emptyList());
        when(locationRepositoryFacade.getTerrainsForCampaign(invalidCampaignId)).thenReturn(Collections.emptyList());
        when(locationRepositoryFacade.getClimatesForCampaign(invalidCampaignId)).thenReturn(Collections.emptyList());

        LocationOverviewDTO result = locationOverviewService.getLocationOverview(invalidCampaignId);

        // Assertions: All lists should be empty
        assertNotNull(result);
        assertTrue(result.getContinents().isEmpty());
        assertTrue(result.getCountries().isEmpty());
        assertTrue(result.getCities().isEmpty());
        assertTrue(result.getRegions().isEmpty());
        assertTrue(result.getLandmarks().isEmpty());
        assertTrue(result.getPlaces().isEmpty());
        assertTrue(result.getPlaceTypes().isEmpty());
        assertTrue(result.getTerrains().isEmpty());
        assertTrue(result.getClimates().isEmpty());

        // Verify methods were called
        verify(locationRepositoryFacade).findContinentsByCampaign(invalidCampaignId);
        verify(locationRepositoryFacade).findCountriesByCampaign(invalidCampaignId);
        verify(locationRepositoryFacade).findCitiesByCampaign(invalidCampaignId);
        verify(locationRepositoryFacade).findRegionsByCampaign(invalidCampaignId);
        verify(locationRepositoryFacade).findLandmarksByCampaign(invalidCampaignId);
        verify(locationRepositoryFacade).findPlacesByCampaign(invalidCampaignId);
        verify(locationRepositoryFacade).getPlaceTypesForCampaign(invalidCampaignId);
        verify(locationRepositoryFacade).getTerrainsForCampaign(invalidCampaignId);
        verify(locationRepositoryFacade).getClimatesForCampaign(invalidCampaignId);
    }
}
