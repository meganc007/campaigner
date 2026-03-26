package com.mcommings.campaigner.controllers.calendar;

import com.mcommings.campaigner.controllers.BaseControllerTest;
import com.mcommings.campaigner.modules.calendar.controllers.WeekController;
import com.mcommings.campaigner.modules.calendar.dtos.weeks.CreateWeekDTO;
import com.mcommings.campaigner.modules.calendar.dtos.weeks.UpdateWeekDTO;
import com.mcommings.campaigner.modules.calendar.dtos.weeks.ViewWeekDTO;
import com.mcommings.campaigner.modules.calendar.services.WeekService;
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

@WebMvcTest(WeekController.class)
class WeekControllerTest extends BaseControllerTest {

    @MockitoBean
    WeekService weekService;

    //GET all
    @Test
    void getWeeks_returnsList() throws Exception {

        when(weekService.getAll())
                .thenReturn(List.of(CalendarTestDataFactory.viewWeekDTO()));

        get("/api/weeks")
                .andExpect(status().isOk());

        verify(weekService).getAll();
    }

    //GET by id
    @Test
    void getWeek_returnsWeek() throws Exception {

        ViewWeekDTO dto = CalendarTestDataFactory.viewWeekDTO();

        when(weekService.getById(1)).thenReturn(dto);

        get("/api/weeks/1")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()));
    }

    //GET by MonthId
    @Test
    void getWeeksByMonthId_returnsWeeks() throws Exception {

        when(weekService.getWeeksByMonthId(CalendarTestConstants.MONTH_ID))
                .thenReturn(List.of(CalendarTestDataFactory.viewWeekDTO()));

        get("/api/weeks/month/" + CalendarTestConstants.MONTH_ID)
                .andExpect(status().isOk());

        verify(weekService)
                .getWeeksByMonthId(CalendarTestConstants.MONTH_ID);
    }

    //GET by CampaignUUID
    @Test
    void getWeeksByCampaignUUID_returnsWeeks() throws Exception {

        when(weekService.getWeeksByCampaignUUID(CalendarTestConstants.CAMPAIGN_UUID))
                .thenReturn(List.of(CalendarTestDataFactory.viewWeekDTO()));

        get("/api/weeks/campaign/" + CalendarTestConstants.CAMPAIGN_UUID)
                .andExpect(status().isOk());

        verify(weekService)
                .getWeeksByCampaignUUID(CalendarTestConstants.CAMPAIGN_UUID);
    }

    //POST
    @Test
    void createWeek_returnsSaved() throws Exception {

        CreateWeekDTO create =
                CalendarTestDataFactory.createWeekDTO();

        ViewWeekDTO response =
                CalendarTestDataFactory.viewWeekDTO();

        when(weekService.create(any())).thenReturn(response);

        post("/api/weeks", create)
                .andExpect(status().isOk());
    }

    //PUT
    @Test
    void updateWeek_returnsUpdated() throws Exception {

        UpdateWeekDTO update =
                CalendarTestDataFactory.updateWeekDTO();

        ViewWeekDTO response =
                CalendarTestDataFactory.viewWeekDTO();

        when(weekService.update(any())).thenReturn(response);

        put("/api/weeks", update)
                .andExpect(status().isOk());
    }

    //DELETE
    @Test
    void deleteWeek_returnsOk() throws Exception {

        delete("/api/weeks/1")
                .andExpect(status().isOk());

        verify(weekService).delete(1);
    }
}