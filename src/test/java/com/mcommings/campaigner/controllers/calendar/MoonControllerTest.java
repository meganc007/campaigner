package com.mcommings.campaigner.controllers.calendar;

import com.mcommings.campaigner.controllers.BaseControllerTest;
import com.mcommings.campaigner.modules.calendar.controllers.MoonController;
import com.mcommings.campaigner.modules.calendar.dtos.moons.CreateMoonDTO;
import com.mcommings.campaigner.modules.calendar.dtos.moons.UpdateMoonDTO;
import com.mcommings.campaigner.modules.calendar.dtos.moons.ViewMoonDTO;
import com.mcommings.campaigner.modules.calendar.services.MoonService;
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

@WebMvcTest(MoonController.class)
class MoonControllerTest extends BaseControllerTest {

    @MockitoBean
    MoonService moonService;

    //GET all
    @Test
    void getMoons_returnsList() throws Exception {

        when(moonService.getAll())
                .thenReturn(List.of(CalendarTestDataFactory.viewMoonDTO()));

        get("/api/moons")
                .andExpect(status().isOk());

        verify(moonService).getAll();
    }

    //GET by id
    @Test
    void getMoon_returnsMoon() throws Exception {

        ViewMoonDTO dto = CalendarTestDataFactory.viewMoonDTO();

        when(moonService.getById(1)).thenReturn(dto);

        get("/api/moons/1")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()));
    }

    //GET by CampaignUUID
    @Test
    void getMoonsByCampaignUUID_returnsMoons() throws Exception {

        when(moonService.getMoonsByCampaignUUID(CalendarTestConstants.CAMPAIGN_UUID))
                .thenReturn(List.of(CalendarTestDataFactory.viewMoonDTO()));

        get("/api/moons/campaign/" + CalendarTestConstants.CAMPAIGN_UUID)
                .andExpect(status().isOk());

        verify(moonService)
                .getMoonsByCampaignUUID(CalendarTestConstants.CAMPAIGN_UUID);
    }

    //POST
    @Test
    void createMoon_returnsSaved() throws Exception {

        CreateMoonDTO create =
                CalendarTestDataFactory.createMoonDTO();

        ViewMoonDTO response =
                CalendarTestDataFactory.viewMoonDTO();

        when(moonService.create(any())).thenReturn(response);

        post("/api/moons", create)
                .andExpect(status().isOk());
    }

    //PUT
    @Test
    void updateMoon_returnsUpdated() throws Exception {

        UpdateMoonDTO update =
                CalendarTestDataFactory.updateMoonDTO();

        ViewMoonDTO response =
                CalendarTestDataFactory.viewMoonDTO();

        when(moonService.update(any())).thenReturn(response);

        put("/api/moons", update)
                .andExpect(status().isOk());
    }

    //DELETE
    @Test
    void deleteMoon_returnsOk() throws Exception {

        delete("/api/moons/1")
                .andExpect(status().isOk());

        verify(moonService).delete(1);
    }
}