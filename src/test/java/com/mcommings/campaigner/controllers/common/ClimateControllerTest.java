package com.mcommings.campaigner.controllers.common;

import com.mcommings.campaigner.controllers.BaseControllerTest;
import com.mcommings.campaigner.modules.common.controllers.ClimateController;
import com.mcommings.campaigner.modules.common.dtos.climate.CreateClimateDTO;
import com.mcommings.campaigner.modules.common.dtos.climate.UpdateClimateDTO;
import com.mcommings.campaigner.modules.common.dtos.climate.ViewClimateDTO;
import com.mcommings.campaigner.modules.common.services.ClimateService;
import com.mcommings.campaigner.setup.common.factories.CommonTestDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClimateController.class)
public class ClimateControllerTest extends BaseControllerTest {

    @MockitoBean
    ClimateService climateService;

    //GET all
    @Test
    void getClimates_returnsList() throws Exception {

        when(climateService.getAll())
                .thenReturn(List.of(CommonTestDataFactory.viewClimateDTO()));

        get("/api/climates")
                .andExpect(status().isOk());

        verify(climateService).getAll();
    }

    //GET by id
    @Test
    void getClimate_returnsClimate() throws Exception {

        ViewClimateDTO dto = CommonTestDataFactory.viewClimateDTO();

        when(climateService.getById(1)).thenReturn(dto);

        get("/api/climates/1")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()));
    }

    //POST
    @Test
    void createClimate_returnsSaved() throws Exception {

        CreateClimateDTO create =
                CommonTestDataFactory.createClimateDTO();

        ViewClimateDTO response =
                CommonTestDataFactory.viewClimateDTO();

        when(climateService.create(any())).thenReturn(response);

        post("/api/climates", create)
                .andExpect(status().isOk());
    }

    //PUT
    @Test
    void updateClimate_returnsUpdated() throws Exception {

        UpdateClimateDTO update =
                CommonTestDataFactory.updateClimateDTO();

        ViewClimateDTO response =
                CommonTestDataFactory.viewClimateDTO();

        when(climateService.update(any())).thenReturn(response);

        put("/api/climates", update)
                .andExpect(status().isOk());
    }

    //DELETE
    @Test
    void deleteClimate_returnsOk() throws Exception {

        delete("/api/climates/1")
                .andExpect(status().isOk());

        verify(climateService).delete(1);
    }
}
