package com.mcommings.campaigner.controllers.locations;

import com.mcommings.campaigner.controllers.BaseControllerTest;
import com.mcommings.campaigner.modules.locations.controllers.ContinentController;
import com.mcommings.campaigner.modules.locations.dtos.continents.CreateContinentDTO;
import com.mcommings.campaigner.modules.locations.dtos.continents.UpdateContinentDTO;
import com.mcommings.campaigner.modules.locations.dtos.continents.ViewContinentDTO;
import com.mcommings.campaigner.modules.locations.services.ContinentService;
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

@WebMvcTest(ContinentController.class)
public class ContinentControllerTest extends BaseControllerTest {

    @MockitoBean
    private ContinentService continentService;

    //GET all
    @Test
    void getContinents_returnsList() throws Exception {

        when(continentService.getAll())
                .thenReturn(List.of(LocationsTestDataFactory.viewContinentDTO()));

        get("/api/continents")
                .andExpect(status().isOk());

        verify(continentService).getAll();
    }

    //GET by id
    @Test
    void getContinent_returnsContinent() throws Exception {

        ViewContinentDTO dto = LocationsTestDataFactory.viewContinentDTO();

        when(continentService.getById(1)).thenReturn(dto);

        get("/api/continents/1")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()));
    }

    //GET by CampaignUUID
    @Test
    void getContinentsByCampaignUUID_returnsContinents() throws Exception {

        when(continentService.getContinentsByCampaignUUID(LocationsTestConstants.CAMPAIGN_UUID))
                .thenReturn(List.of(LocationsTestDataFactory.viewContinentDTO()));

        get("/api/continents/campaign/" + LocationsTestConstants.CAMPAIGN_UUID)
                .andExpect(status().isOk());

        verify(continentService)
                .getContinentsByCampaignUUID(LocationsTestConstants.CAMPAIGN_UUID);
    }

    //POST
    @Test
    void createContinent_returnsSaved() throws Exception {

        CreateContinentDTO create =
                LocationsTestDataFactory.createContinentDTO();

        ViewContinentDTO response =
                LocationsTestDataFactory.viewContinentDTO();

        when(continentService.create(any())).thenReturn(response);

        post("/api/continents", create)
                .andExpect(status().isOk());
    }

    //PUT
    @Test
    void updateContinent_returnsUpdated() throws Exception {

        UpdateContinentDTO update =
                LocationsTestDataFactory.updateContinentDTO();

        ViewContinentDTO response =
                LocationsTestDataFactory.viewContinentDTO();

        when(continentService.update(any())).thenReturn(response);

        put("/api/continents", update)
                .andExpect(status().isOk());
    }

    //DELETE
    @Test
    void deleteContinent_returnsOk() throws Exception {

        delete("/api/continents/1")
                .andExpect(status().isOk());

        verify(continentService).delete(1);
    }
}
