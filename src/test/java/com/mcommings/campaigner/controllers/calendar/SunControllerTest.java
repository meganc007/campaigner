package com.mcommings.campaigner.controllers.calendar;

import com.mcommings.campaigner.controllers.BaseControllerTest;
import com.mcommings.campaigner.modules.calendar.controllers.SunController;
import com.mcommings.campaigner.modules.calendar.dtos.suns.CreateSunDTO;
import com.mcommings.campaigner.modules.calendar.dtos.suns.UpdateSunDTO;
import com.mcommings.campaigner.modules.calendar.dtos.suns.ViewSunDTO;
import com.mcommings.campaigner.modules.calendar.services.SunService;
import com.mcommings.campaigner.setup.calendar.factories.CalendarTestDataFactory;
import com.mcommings.campaigner.setup.calendar.fixtures.CalendarTestConstants;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SunController.class)
class SunControllerTest extends BaseControllerTest {

    @MockitoBean
    SunService sunService;

    //GET all
    @Test
    void getSuns_returnsList() throws Exception {

        when(sunService.getAll())
                .thenReturn(List.of(CalendarTestDataFactory.viewSunDTO()));

        get("/api/suns")
                .andExpect(status().isOk());

        verify(sunService).getAll();
    }

    //GET by id
    @Test
    void getSun_returnsSun() throws Exception {

        ViewSunDTO dto = CalendarTestDataFactory.viewSunDTO();

        when(sunService.getById(1)).thenReturn(dto);

        get("/api/suns/1")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()));
    }

    //GET by CampaignUUID
    @Test
    void getSunsByCampaignUUID_returnsSuns() throws Exception {

        when(sunService.getSunsByCampaignUUID(CalendarTestConstants.CAMPAIGN_UUID))
                .thenReturn(List.of(CalendarTestDataFactory.viewSunDTO()));

        get("/api/suns/campaign/" + CalendarTestConstants.CAMPAIGN_UUID)
                .andExpect(status().isOk());

        verify(sunService)
                .getSunsByCampaignUUID(CalendarTestConstants.CAMPAIGN_UUID);
    }

    //POST
    @Test
    void createSun_returnsSaved() throws Exception {

        CreateSunDTO create =
                CalendarTestDataFactory.createSunDTO();

        ViewSunDTO response =
                CalendarTestDataFactory.viewSunDTO();

        when(sunService.create(any())).thenReturn(response);

        post("/api/suns", create)
                .andExpect(status().isOk());
    }

    //PUT
    @Test
    void updateSun_returnsUpdated() throws Exception {

        UpdateSunDTO update =
                CalendarTestDataFactory.updateSunDTO();

        ViewSunDTO response =
                CalendarTestDataFactory.viewSunDTO();

        when(sunService.update(any())).thenReturn(response);

        put("/api/suns", update)
                .andExpect(status().isOk());
    }

    //DELETE
    @Test
    void deleteSun_returnsOk() throws Exception {

        delete("/api/suns/1")
                .andExpect(status().isOk());

        verify(sunService).delete(1);
    }
}