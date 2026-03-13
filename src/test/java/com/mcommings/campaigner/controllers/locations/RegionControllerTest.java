package com.mcommings.campaigner.controllers.locations;

import com.mcommings.campaigner.controllers.BaseControllerTest;
import com.mcommings.campaigner.modules.locations.controllers.RegionController;
import com.mcommings.campaigner.modules.locations.dtos.regions.CreateRegionDTO;
import com.mcommings.campaigner.modules.locations.dtos.regions.UpdateRegionDTO;
import com.mcommings.campaigner.modules.locations.dtos.regions.ViewRegionDTO;
import com.mcommings.campaigner.modules.locations.services.RegionService;
import com.mcommings.campaigner.setup.locations.factories.LocationsTestDataFactory;
import com.mcommings.campaigner.setup.locations.fixtures.LocationsTestConstants;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RegionController.class)
public class RegionControllerTest extends BaseControllerTest {

    @MockitoBean
    RegionService regionService;

    //GET all
    @Test
    void getRegions_returnsList() throws Exception {

        when(regionService.getAll())
                .thenReturn(List.of(LocationsTestDataFactory.viewRegionDTO()));

        get("/api/regions")
                .andExpect(status().isOk());

        verify(regionService).getAll();
    }

    //GET by id
    @Test
    void getRegion_returnsRegion() throws Exception {

        ViewRegionDTO dto = LocationsTestDataFactory.viewRegionDTO();

        when(regionService.getById(1)).thenReturn(dto);

        get("/api/regions/1")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()));
    }

    //GET by ClimateId
    @Test
    void getRegionsByClimateId_returnsRegions() throws Exception {

        when(regionService.getRegionsByClimateId(LocationsTestConstants.CLIMATE_ID))
                .thenReturn(List.of(LocationsTestDataFactory.viewRegionDTO()));

        get("/api/regions/climate/" + LocationsTestConstants.CLIMATE_ID)
                .andExpect(status().isOk());

        verify(regionService)
                .getRegionsByClimateId(LocationsTestConstants.CLIMATE_ID);
    }

    //GET by CountryId
    @Test
    void getRegionsByCountryId_returnsRegions() throws Exception {

        when(regionService.getRegionsByCountryId(LocationsTestConstants.COUNTRY_ID))
                .thenReturn(List.of(LocationsTestDataFactory.viewRegionDTO()));

        get("/api/regions/country/" + LocationsTestConstants.COUNTRY_ID)
                .andExpect(status().isOk());

        verify(regionService)
                .getRegionsByCountryId(LocationsTestConstants.COUNTRY_ID);
    }

    //GET by CampaignUUID
    @Test
    void getRegionsByCampaignUUID_returnsRegions() throws Exception {

        when(regionService.getRegionsByCampaignUUID(LocationsTestConstants.CAMPAIGN_UUID))
                .thenReturn(List.of(LocationsTestDataFactory.viewRegionDTO()));

        get("/api/regions/campaign/" + LocationsTestConstants.CAMPAIGN_UUID)
                .andExpect(status().isOk());

        verify(regionService)
                .getRegionsByCampaignUUID(LocationsTestConstants.CAMPAIGN_UUID);
    }

    //POST
    @Test
    void createRegion_returnsSaved() throws Exception {

        CreateRegionDTO create =
                LocationsTestDataFactory.createRegionDTO();

        ViewRegionDTO response =
                LocationsTestDataFactory.viewRegionDTO();

        when(regionService.create(any())).thenReturn(response);

        post("/api/regions", create)
                .andExpect(status().isOk());
    }

    //PUT
    @Test
    void updateRegion_returnsUpdated() throws Exception {

        UpdateRegionDTO update =
                LocationsTestDataFactory.updateRegionDTO();

        ViewRegionDTO response =
                LocationsTestDataFactory.viewRegionDTO();

        when(regionService.update(any())).thenReturn(response);

        put("/api/regions", update)
                .andExpect(status().isOk());
    }

    //DELETE
    @Test
    void deleteRegion_returnsOk() throws Exception {

        delete("/api/regions/1")
                .andExpect(status().isOk());

        verify(regionService).delete(1);
    }
}
