package com.mcommings.campaigner.controllers.calendar;

import com.mcommings.campaigner.controllers.BaseControllerTest;
import com.mcommings.campaigner.modules.calendar.controllers.DayController;
import com.mcommings.campaigner.modules.calendar.dtos.days.CreateDayDTO;
import com.mcommings.campaigner.modules.calendar.dtos.days.UpdateDayDTO;
import com.mcommings.campaigner.modules.calendar.dtos.days.ViewDayDTO;
import com.mcommings.campaigner.modules.calendar.services.DayService;
import com.mcommings.campaigner.setup.calendar.factories.CalendarTestDataFactory;
import com.mcommings.campaigner.setup.calendar.fixtures.CalendarTestConstants;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DayController.class)
class DayControllerTest extends BaseControllerTest {

    @MockitoBean
    DayService dayService;

    //GET all
    @Test
    void getDays_returnsList() throws Exception {

        when(dayService.getAll())
                .thenReturn(List.of(CalendarTestDataFactory.viewDayDTO()));

        get("/api/days")
                .andExpect(status().isOk());

        verify(dayService).getAll();
    }

    //GET by id
    @Test
    void getDay_returnsDay() throws Exception {

        ViewDayDTO dto = CalendarTestDataFactory.viewDayDTO();

        when(dayService.getById(1)).thenReturn(dto);

        get("/api/days/1")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()));
    }

    //GET by WeekId
    @Test
    void getDaysByWeekId_returnsDays() throws Exception {

        when(dayService.getDaysByWeekId(CalendarTestConstants.WEEK_ID))
                .thenReturn(List.of(CalendarTestDataFactory.viewDayDTO()));

        get("/api/days/week/" + CalendarTestConstants.WEEK_ID)
                .andExpect(status().isOk());

        verify(dayService)
                .getDaysByWeekId(CalendarTestConstants.WEEK_ID);
    }

    //GET by CampaignUUID
    @Test
    void getDaysByCampaignUUID_returnsDays() throws Exception {

        when(dayService.getDaysByCampaignUUID(CalendarTestConstants.CAMPAIGN_UUID))
                .thenReturn(List.of(CalendarTestDataFactory.viewDayDTO()));

        get("/api/days/campaign/" + CalendarTestConstants.CAMPAIGN_UUID)
                .andExpect(status().isOk());

        verify(dayService)
                .getDaysByCampaignUUID(CalendarTestConstants.CAMPAIGN_UUID);
    }

    //POST
    @Test
    void createDay_returnsSaved() throws Exception {

        CreateDayDTO create =
                CalendarTestDataFactory.createDayDTO();

        ViewDayDTO response =
                CalendarTestDataFactory.viewDayDTO();

        when(dayService.create(any())).thenReturn(response);

        post("/api/days", create)
                .andExpect(status().isOk());
    }

    //PUT
    @Test
    void updateDay_returnsUpdated() throws Exception {

        UpdateDayDTO update =
                CalendarTestDataFactory.updateDayDTO();

        ViewDayDTO response =
                CalendarTestDataFactory.viewDayDTO();

        when(dayService.update(any())).thenReturn(response);

        put("/api/days", update)
                .andExpect(status().isOk());
    }

    //DELETE
    @Test
    void deleteDay_returnsOk() throws Exception {

        delete("/api/days/1")
                .andExpect(status().isOk());

        verify(dayService).delete(1);
    }
}