package com.mcommings.campaigner.controllers.calendar;

import com.mcommings.campaigner.controllers.BaseControllerTest;
import com.mcommings.campaigner.modules.calendar.controllers.MonthController;
import com.mcommings.campaigner.modules.calendar.dtos.months.CreateMonthDTO;
import com.mcommings.campaigner.modules.calendar.dtos.months.UpdateMonthDTO;
import com.mcommings.campaigner.modules.calendar.dtos.months.ViewMonthDTO;
import com.mcommings.campaigner.modules.calendar.services.MonthService;
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

@WebMvcTest(MonthController.class)
class MonthControllerTest extends BaseControllerTest {

    @MockitoBean
    MonthService monthService;

    //GET all
    @Test
    void getMonths_returnsList() throws Exception {

        when(monthService.getAll())
                .thenReturn(List.of(CalendarTestDataFactory.viewMonthDTO()));

        get("/api/months")
                .andExpect(status().isOk());

        verify(monthService).getAll();
    }

    //GET by id
    @Test
    void getMonth_returnsMonth() throws Exception {

        ViewMonthDTO dto = CalendarTestDataFactory.viewMonthDTO();

        when(monthService.getById(1)).thenReturn(dto);

        get("/api/months/1")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()));
    }

    //GET by CampaignUUID
    @Test
    void getMonthsByCampaignUUID_returnsMonths() throws Exception {

        when(monthService.getMonthsByCampaignUUID(CalendarTestConstants.CAMPAIGN_UUID))
                .thenReturn(List.of(CalendarTestDataFactory.viewMonthDTO()));

        get("/api/months/campaign/" + CalendarTestConstants.CAMPAIGN_UUID)
                .andExpect(status().isOk());

        verify(monthService)
                .getMonthsByCampaignUUID(CalendarTestConstants.CAMPAIGN_UUID);
    }

    //POST
    @Test
    void createMonth_returnsSaved() throws Exception {

        CreateMonthDTO create =
                CalendarTestDataFactory.createMonthDTO();

        ViewMonthDTO response =
                CalendarTestDataFactory.viewMonthDTO();

        when(monthService.create(any())).thenReturn(response);

        post("/api/months", create)
                .andExpect(status().isOk());
    }

    //PUT
    @Test
    void updateMonth_returnsUpdated() throws Exception {

        UpdateMonthDTO update =
                CalendarTestDataFactory.updateMonthDTO();

        ViewMonthDTO response =
                CalendarTestDataFactory.viewMonthDTO();

        when(monthService.update(any())).thenReturn(response);

        put("/api/months", update)
                .andExpect(status().isOk());
    }

    //DELETE
    @Test
    void deleteMonth_returnsOk() throws Exception {

        delete("/api/months/1")
                .andExpect(status().isOk());

        verify(monthService).delete(1);
    }
}