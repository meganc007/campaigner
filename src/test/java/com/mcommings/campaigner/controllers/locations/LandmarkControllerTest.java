package com.mcommings.campaigner.controllers.locations;

import com.mcommings.campaigner.controllers.BaseControllerTest;
import com.mcommings.campaigner.modules.locations.controllers.LandmarkController;
import com.mcommings.campaigner.modules.locations.dtos.landmarks.CreateLandmarkDTO;
import com.mcommings.campaigner.modules.locations.dtos.landmarks.UpdateLandmarkDTO;
import com.mcommings.campaigner.modules.locations.dtos.landmarks.ViewLandmarkDTO;
import com.mcommings.campaigner.modules.locations.services.LandmarkService;
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

@WebMvcTest(LandmarkController.class)
public class LandmarkControllerTest extends BaseControllerTest {
    
    @MockitoBean
    LandmarkService landmarkService;

    //GET all
    @Test
    void getLandmarks_returnsList() throws Exception {

        when(landmarkService.getAll())
                .thenReturn(List.of(LocationsTestDataFactory.viewLandmarkDTO()));

        get("/api/landmarks")
                .andExpect(status().isOk());

        verify(landmarkService).getAll();
    }

    //GET by id
    @Test
    void getLandmark_returnsLandmark() throws Exception {

        ViewLandmarkDTO dto = LocationsTestDataFactory.viewLandmarkDTO();

        when(landmarkService.getById(1)).thenReturn(dto);

        get("/api/landmarks/1")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()));
    }

    //GET by RegionId
    @Test
    void getLandmarksByRegionId_returnsLandmarks() throws Exception {

        when(landmarkService.getLandmarksByRegionId(LocationsTestConstants.REGION_ID))
                .thenReturn(List.of(LocationsTestDataFactory.viewLandmarkDTO()));

        get("/api/landmarks/region/" + LocationsTestConstants.REGION_ID)
                .andExpect(status().isOk());

        verify(landmarkService)
                .getLandmarksByRegionId(LocationsTestConstants.REGION_ID);
    }


    //GET by CampaignUUID
    @Test
    void getLandmarksByCampaignUUID_returnsLandmarks() throws Exception {

        when(landmarkService.getLandmarksByCampaignUUID(LocationsTestConstants.CAMPAIGN_UUID))
                .thenReturn(List.of(LocationsTestDataFactory.viewLandmarkDTO()));

        get("/api/landmarks/campaign/" + LocationsTestConstants.CAMPAIGN_UUID)
                .andExpect(status().isOk());

        verify(landmarkService)
                .getLandmarksByCampaignUUID(LocationsTestConstants.CAMPAIGN_UUID);
    }

    //POST
    @Test
    void createLandmark_returnsSaved() throws Exception {

        CreateLandmarkDTO create =
                LocationsTestDataFactory.createLandmarkDTO();

        ViewLandmarkDTO response =
                LocationsTestDataFactory.viewLandmarkDTO();

        when(landmarkService.create(any())).thenReturn(response);

        post("/api/landmarks", create)
                .andExpect(status().isOk());
    }

    //PUT
    @Test
    void updateLandmark_returnsUpdated() throws Exception {

        UpdateLandmarkDTO update =
                LocationsTestDataFactory.updateLandmarkDTO();

        ViewLandmarkDTO response =
                LocationsTestDataFactory.viewLandmarkDTO();

        when(landmarkService.update(any())).thenReturn(response);

        put("/api/landmarks", update)
                .andExpect(status().isOk());
    }

    //DELETE
    @Test
    void deleteLandmark_returnsOk() throws Exception {

        delete("/api/landmarks/1")
                .andExpect(status().isOk());

        verify(landmarkService).delete(1);
    }
}
