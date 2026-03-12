package com.mcommings.campaigner.controllers.locations;

import com.mcommings.campaigner.controllers.BaseControllerTest;
import com.mcommings.campaigner.modules.locations.controllers.CityController;
import com.mcommings.campaigner.modules.locations.dtos.cities.CreateCityDTO;
import com.mcommings.campaigner.modules.locations.dtos.cities.UpdateCityDTO;
import com.mcommings.campaigner.modules.locations.dtos.cities.ViewCityDTO;
import com.mcommings.campaigner.modules.locations.services.CityService;
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

@WebMvcTest(CityController.class)
public class CityControllerTest extends BaseControllerTest {

    @MockitoBean
    CityService cityService;

    //GET all
    @Test
    void getCities_returnsList() throws Exception {

        when(cityService.getAll())
                .thenReturn(List.of(LocationsTestDataFactory.viewCityDTO()));

        get("/api/cities")
                .andExpect(status().isOk());

        verify(cityService).getAll();
    }

    //GET by id
    @Test
    void getCity_returnsCity() throws Exception {

        ViewCityDTO dto = LocationsTestDataFactory.viewCityDTO();

        when(cityService.getById(1)).thenReturn(dto);

        get("/api/cities/1")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()));
    }

    //GET by WealthId
    @Test
    void getCitiesByWealthId_returnsCities() throws Exception {

        when(cityService.getCitiesByWealthId(LocationsTestConstants.WEALTH_ID))
                .thenReturn(List.of(LocationsTestDataFactory.viewCityDTO()));

        get("/api/cities/wealth/" + LocationsTestConstants.WEALTH_ID)
                .andExpect(status().isOk());

        verify(cityService)
                .getCitiesByWealthId(LocationsTestConstants.WEALTH_ID);
    }

    //GET by CountryId
    @Test
    void getCitiesByCountryId_returnsCities() throws Exception {

        when(cityService.getCitiesByCountryId(LocationsTestConstants.COUNTRY_ID))
                .thenReturn(List.of(LocationsTestDataFactory.viewCityDTO()));

        get("/api/cities/country/" + LocationsTestConstants.COUNTRY_ID)
                .andExpect(status().isOk());

        verify(cityService)
                .getCitiesByCountryId(LocationsTestConstants.COUNTRY_ID);
    }

    //GET by SettlementTypeId
    @Test
    void getCitiesBySettlementTypeId_returnsCities() throws Exception {

        when(cityService.getCitiesBySettlementTypeId(LocationsTestConstants.SETTLEMENTTYPE_ID))
                .thenReturn(List.of(LocationsTestDataFactory.viewCityDTO()));

        get("/api/cities/settlementtypes/" + LocationsTestConstants.SETTLEMENTTYPE_ID)
                .andExpect(status().isOk());

        verify(cityService)
                .getCitiesBySettlementTypeId(LocationsTestConstants.SETTLEMENTTYPE_ID);
    }

    //GET by GovernmentId
    @Test
    void getCitiesByGovernmentId_returnsCities() throws Exception {

        when(cityService.getCitiesByGovernmentId(LocationsTestConstants.GOVERNMENT_ID))
                .thenReturn(List.of(LocationsTestDataFactory.viewCityDTO()));

        get("/api/cities/government/" + LocationsTestConstants.GOVERNMENT_ID)
                .andExpect(status().isOk());

        verify(cityService)
                .getCitiesByGovernmentId(LocationsTestConstants.GOVERNMENT_ID);
    }

    //GET by RegionId
    @Test
    void getCitiesByRegionId_returnsCities() throws Exception {

        when(cityService.getCitiesByRegionId(LocationsTestConstants.REGION_ID))
                .thenReturn(List.of(LocationsTestDataFactory.viewCityDTO()));

        get("/api/cities/region/" + LocationsTestConstants.REGION_ID)
                .andExpect(status().isOk());

        verify(cityService)
                .getCitiesByRegionId(LocationsTestConstants.REGION_ID);
    }

    //GET by CampaignUUID
    @Test
    void getCitiesByCampaignUUID_returnsCities() throws Exception {

        when(cityService.getCitiesByCampaignUUID(LocationsTestConstants.CAMPAIGN_UUID))
                .thenReturn(List.of(LocationsTestDataFactory.viewCityDTO()));

        get("/api/cities/campaign/" + LocationsTestConstants.CAMPAIGN_UUID)
                .andExpect(status().isOk());

        verify(cityService)
                .getCitiesByCampaignUUID(LocationsTestConstants.CAMPAIGN_UUID);
    }

    //POST
    @Test
    void createCity_returnsSaved() throws Exception {

        CreateCityDTO create =
                LocationsTestDataFactory.createCityDTO();

        ViewCityDTO response =
                LocationsTestDataFactory.viewCityDTO();

        when(cityService.create(any())).thenReturn(response);

        post("/api/cities", create)
                .andExpect(status().isOk());
    }

    //PUT
    @Test
    void updateCity_returnsUpdated() throws Exception {

        UpdateCityDTO update =
                LocationsTestDataFactory.updateCityDTO();

        ViewCityDTO response =
                LocationsTestDataFactory.viewCityDTO();

        when(cityService.update(any())).thenReturn(response);

        put("/api/cities", update)
                .andExpect(status().isOk());
    }

    //DELETE
    @Test
    void deleteCity_returnsOk() throws Exception {

        delete("/api/cities/1")
                .andExpect(status().isOk());

        verify(cityService).delete(1);
    }
}
