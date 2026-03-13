package com.mcommings.campaigner.controllers.locations;

import com.mcommings.campaigner.controllers.BaseControllerTest;
import com.mcommings.campaigner.modules.locations.controllers.PlaceController;
import com.mcommings.campaigner.modules.locations.dtos.places.CreatePlaceDTO;
import com.mcommings.campaigner.modules.locations.dtos.places.UpdatePlaceDTO;
import com.mcommings.campaigner.modules.locations.dtos.places.ViewPlaceDTO;
import com.mcommings.campaigner.modules.locations.services.PlaceService;
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

@WebMvcTest(PlaceController.class)
public class PlaceControllerTest extends BaseControllerTest {

    @MockitoBean
    PlaceService placeService;

    //GET all
    @Test
    void getPlaces_returnsList() throws Exception {

        when(placeService.getAll())
                .thenReturn(List.of(LocationsTestDataFactory.viewPlaceDTO()));

        get("/api/places")
                .andExpect(status().isOk());

        verify(placeService).getAll();
    }

    //GET by id
    @Test
    void getPlace_returnsPlace() throws Exception {

        ViewPlaceDTO dto = LocationsTestDataFactory.viewPlaceDTO();

        when(placeService.getById(1)).thenReturn(dto);

        get("/api/places/1")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()));
    }

    //GET by PlaceTypeId
    @Test
    void getPlacesByPlaceTypeId_returnsPlaces() throws Exception {

        when(placeService.getPlacesByPlaceTypeId(LocationsTestConstants.PLACETYPE_ID))
                .thenReturn(List.of(LocationsTestDataFactory.viewPlaceDTO()));

        get("/api/places/placetype/" + LocationsTestConstants.PLACETYPE_ID)
                .andExpect(status().isOk());

        verify(placeService)
                .getPlacesByPlaceTypeId(LocationsTestConstants.PLACETYPE_ID);
    }

    //GET by TerrainId
    @Test
    void getPlacesByTerrainId_returnsPlaces() throws Exception {

        when(placeService.getPlacesByTerrainId(LocationsTestConstants.TERRAIN_ID))
                .thenReturn(List.of(LocationsTestDataFactory.viewPlaceDTO()));

        get("/api/places/terrain/" + LocationsTestConstants.TERRAIN_ID)
                .andExpect(status().isOk());

        verify(placeService)
                .getPlacesByTerrainId(LocationsTestConstants.TERRAIN_ID);
    }

    //GET by CountryId
    @Test
    void getPlacesByCountryId_returnsPlaces() throws Exception {

        when(placeService.getPlacesByCountryId(LocationsTestConstants.COUNTRY_ID))
                .thenReturn(List.of(LocationsTestDataFactory.viewPlaceDTO()));

        get("/api/places/country/" + LocationsTestConstants.COUNTRY_ID)
                .andExpect(status().isOk());

        verify(placeService)
                .getPlacesByCountryId(LocationsTestConstants.COUNTRY_ID);
    }

    //GET by CityId
    @Test
    void getPlacesByCityId_returnsPlaces() throws Exception {

        when(placeService.getPlacesByCityId(LocationsTestConstants.CITY_ID))
                .thenReturn(List.of(LocationsTestDataFactory.viewPlaceDTO()));

        get("/api/places/city/" + LocationsTestConstants.CITY_ID)
                .andExpect(status().isOk());

        verify(placeService)
                .getPlacesByCityId(LocationsTestConstants.CITY_ID);
    }

    //GET by RegionId
    @Test
    void getPlacesByRegionId_returnsPlaces() throws Exception {

        when(placeService.getPlacesByRegionId(LocationsTestConstants.REGION_ID))
                .thenReturn(List.of(LocationsTestDataFactory.viewPlaceDTO()));

        get("/api/places/region/" + LocationsTestConstants.REGION_ID)
                .andExpect(status().isOk());

        verify(placeService)
                .getPlacesByRegionId(LocationsTestConstants.REGION_ID);
    }

    //GET by CampaignUUID
    @Test
    void getPlacesByCampaignUUID_returnsPlaces() throws Exception {

        when(placeService.getPlacesByCampaignUUID(LocationsTestConstants.CAMPAIGN_UUID))
                .thenReturn(List.of(LocationsTestDataFactory.viewPlaceDTO()));

        get("/api/places/campaign/" + LocationsTestConstants.CAMPAIGN_UUID)
                .andExpect(status().isOk());

        verify(placeService)
                .getPlacesByCampaignUUID(LocationsTestConstants.CAMPAIGN_UUID);
    }

    //POST
    @Test
    void createPlace_returnsSaved() throws Exception {

        CreatePlaceDTO create =
                LocationsTestDataFactory.createPlaceDTO();

        ViewPlaceDTO response =
                LocationsTestDataFactory.viewPlaceDTO();

        when(placeService.create(any())).thenReturn(response);

        post("/api/places", create)
                .andExpect(status().isOk());
    }

    //PUT
    @Test
    void updatePlace_returnsUpdated() throws Exception {

        UpdatePlaceDTO update =
                LocationsTestDataFactory.updatePlaceDTO();

        ViewPlaceDTO response =
                LocationsTestDataFactory.viewPlaceDTO();

        when(placeService.update(any())).thenReturn(response);

        put("/api/places", update)
                .andExpect(status().isOk());
    }

    //DELETE
    @Test
    void deletePlace_returnsOk() throws Exception {

        delete("/api/places/1")
                .andExpect(status().isOk());

        verify(placeService).delete(1);
    }
}
