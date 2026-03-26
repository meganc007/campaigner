package com.mcommings.campaigner.controllers.calendar;

import com.mcommings.campaigner.controllers.BaseControllerTest;
import com.mcommings.campaigner.modules.calendar.controllers.EventController;
import com.mcommings.campaigner.modules.calendar.dtos.events.CreateEventDTO;
import com.mcommings.campaigner.modules.calendar.dtos.events.UpdateEventDTO;
import com.mcommings.campaigner.modules.calendar.dtos.events.ViewEventDTO;
import com.mcommings.campaigner.modules.calendar.services.EventService;
import com.mcommings.campaigner.setup.calendar.factories.CalendarTestDataFactory;
import com.mcommings.campaigner.setup.calendar.fixtures.CalendarTestConstants;
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

@WebMvcTest(EventController.class)
public class EventControllerTest extends BaseControllerTest {

    @MockitoBean
    private EventService EventService;

    //GET all
    @Test
    void getEvents_returnsList() throws Exception {

        when(EventService.getAll())
                .thenReturn(List.of(CalendarTestDataFactory.viewEventDTO()));

        get("/api/events")
                .andExpect(status().isOk());

        verify(EventService).getAll();
    }

    //GET by id
    @Test
    void getEvent_returnsEvent() throws Exception {

        ViewEventDTO dto = CalendarTestDataFactory.viewEventDTO();

        when(EventService.getById(1)).thenReturn(dto);

        get("/api/events/1")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()));
    }

    //GET by MonthId
    @Test
    void getEventsByMonthId_returnsEvents() throws Exception {

        when(EventService.getEventsByMonthId(CalendarTestConstants.MONTH_ID))
                .thenReturn(List.of(CalendarTestDataFactory.viewEventDTO()));

        get("/api/events/month/" + CalendarTestConstants.MONTH_ID)
                .andExpect(status().isOk());

        verify(EventService)
                .getEventsByMonthId(CalendarTestConstants.MONTH_ID);
    }

    //GET by WeekId
    @Test
    void getEventsByWeekId_returnsEvents() throws Exception {

        when(EventService.getEventsByWeekId(CalendarTestConstants.WEEK_ID))
                .thenReturn(List.of(CalendarTestDataFactory.viewEventDTO()));

        get("/api/events/week/" + CalendarTestConstants.WEEK_ID)
                .andExpect(status().isOk());

        verify(EventService)
                .getEventsByWeekId(CalendarTestConstants.WEEK_ID);
    }

    //GET by DayId
    @Test
    void getEventsByDayId_returnsEvents() throws Exception {

        when(EventService.getEventsByDayId(CalendarTestConstants.DAY_ID))
                .thenReturn(List.of(CalendarTestDataFactory.viewEventDTO()));

        get("/api/events/day/" + CalendarTestConstants.DAY_ID)
                .andExpect(status().isOk());

        verify(EventService)
                .getEventsByDayId(CalendarTestConstants.DAY_ID);
    }

    //GET by Year
    @Test
    void getEventsByYear_returnsEvents() throws Exception {

        when(EventService.getEventsByYear(CalendarTestConstants.EVENT_YEAR))
                .thenReturn(List.of(CalendarTestDataFactory.viewEventDTO()));

        get("/api/events/year/" + CalendarTestConstants.EVENT_YEAR)
                .andExpect(status().isOk());

        verify(EventService)
                .getEventsByYear(CalendarTestConstants.EVENT_YEAR);
    }

    //GET by ContinentId
    @Test
    void getEventsByContinentId_returnsEvents() throws Exception {

        when(EventService.getEventsByContinentId(LocationsTestConstants.CONTINENT_ID))
                .thenReturn(List.of(CalendarTestDataFactory.viewEventDTO()));

        get("/api/events/continent/" + LocationsTestConstants.CONTINENT_ID)
                .andExpect(status().isOk());

        verify(EventService)
                .getEventsByContinentId(LocationsTestConstants.CONTINENT_ID);
    }

    //GET by CountryId
    @Test
    void getEventsByCountryId_returnsEvents() throws Exception {

        when(EventService.getEventsByCountryId(LocationsTestConstants.COUNTRY_ID))
                .thenReturn(List.of(CalendarTestDataFactory.viewEventDTO()));

        get("/api/events/country/" + LocationsTestConstants.COUNTRY_ID)
                .andExpect(status().isOk());

        verify(EventService)
                .getEventsByCountryId(LocationsTestConstants.COUNTRY_ID);
    }

    //GET by CityId
    @Test
    void getEventsByCityId_returnsEvents() throws Exception {

        when(EventService.getEventsByCityId(LocationsTestConstants.CITY_ID))
                .thenReturn(List.of(CalendarTestDataFactory.viewEventDTO()));

        get("/api/events/city/" + LocationsTestConstants.CITY_ID)
                .andExpect(status().isOk());

        verify(EventService)
                .getEventsByCityId(LocationsTestConstants.CITY_ID);
    }

    //GET by CampaignUUID
    @Test
    void getEventsByCampaignUUID_returnsEvents() throws Exception {

        when(EventService.getEventsByCampaignUUID(CalendarTestConstants.CAMPAIGN_UUID))
                .thenReturn(List.of(CalendarTestDataFactory.viewEventDTO()));

        get("/api/events/campaign/" + CalendarTestConstants.CAMPAIGN_UUID)
                .andExpect(status().isOk());

        verify(EventService)
                .getEventsByCampaignUUID(CalendarTestConstants.CAMPAIGN_UUID);
    }

    //POST
    @Test
    void createEvent_returnsSaved() throws Exception {

        CreateEventDTO create =
                CalendarTestDataFactory.createEventDTO();

        ViewEventDTO response =
                CalendarTestDataFactory.viewEventDTO();

        when(EventService.create(any())).thenReturn(response);

        post("/api/events", create)
                .andExpect(status().isOk());
    }

    //PUT
    @Test
    void updateEvent_returnsUpdated() throws Exception {

        UpdateEventDTO update =
                CalendarTestDataFactory.updateEventDTO();

        ViewEventDTO response =
                CalendarTestDataFactory.viewEventDTO();

        when(EventService.update(any())).thenReturn(response);

        put("/api/events", update)
                .andExpect(status().isOk());
    }

    //DELETE
    @Test
    void deleteEvent_returnsOk() throws Exception {

        delete("/api/events/1")
                .andExpect(status().isOk());

        verify(EventService).delete(1);
    }
}
