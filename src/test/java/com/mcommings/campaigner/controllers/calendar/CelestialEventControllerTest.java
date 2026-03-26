package com.mcommings.campaigner.controllers.calendar;

import com.mcommings.campaigner.controllers.BaseControllerTest;
import com.mcommings.campaigner.modules.calendar.controllers.CelestialEventController;
import com.mcommings.campaigner.modules.calendar.dtos.celestial_events.CreateCelestialEventDTO;
import com.mcommings.campaigner.modules.calendar.dtos.celestial_events.UpdateCelestialEventDTO;
import com.mcommings.campaigner.modules.calendar.dtos.celestial_events.ViewCelestialEventDTO;
import com.mcommings.campaigner.modules.calendar.services.CelestialEventService;
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

@WebMvcTest(CelestialEventController.class)
class CelestialEventControllerTest extends BaseControllerTest {
    
    @MockitoBean
    private CelestialEventService celestialEventService;

    //GET all
    @Test
    void getCelestialEvents_returnsList() throws Exception {

        when(celestialEventService.getAll())
                .thenReturn(List.of(CalendarTestDataFactory.viewCelestialEventDTO()));

        get("/api/celestialevents")
                .andExpect(status().isOk());

        verify(celestialEventService).getAll();
    }

    //GET by id
    @Test
    void getCelestialEvent_returnsCelestialEvent() throws Exception {

        ViewCelestialEventDTO dto = CalendarTestDataFactory.viewCelestialEventDTO();

        when(celestialEventService.getById(1)).thenReturn(dto);

        get("/api/celestialevents/1")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()));
    }

    //GET by MoonId
    @Test
    void getCelestialEventsByMoonId_returnsCelestialEvents() throws Exception {

        when(celestialEventService.getCelestialEventsByMoonId(CalendarTestConstants.MOON_ID))
                .thenReturn(List.of(CalendarTestDataFactory.viewCelestialEventDTO()));

        get("/api/celestialevents/moon/" + CalendarTestConstants.MOON_ID)
                .andExpect(status().isOk());

        verify(celestialEventService)
                .getCelestialEventsByMoonId(CalendarTestConstants.MOON_ID);
    }

    //GET by SunId
    @Test
    void getCelestialEventsBySunId_returnsCelestialEvents() throws Exception {

        when(celestialEventService.getCelestialEventsBySunId(CalendarTestConstants.SUN_ID))
                .thenReturn(List.of(CalendarTestDataFactory.viewCelestialEventDTO()));

        get("/api/celestialevents/sun/" + CalendarTestConstants.SUN_ID)
                .andExpect(status().isOk());

        verify(celestialEventService)
                .getCelestialEventsBySunId(CalendarTestConstants.SUN_ID);
    }

    //GET by MonthId
    @Test
    void getCelestialEventsByMonthId_returnsCelestialEvents() throws Exception {

        when(celestialEventService.getCelestialEventsByMonthId(CalendarTestConstants.MONTH_ID))
                .thenReturn(List.of(CalendarTestDataFactory.viewCelestialEventDTO()));

        get("/api/celestialevents/month/" + CalendarTestConstants.MONTH_ID)
                .andExpect(status().isOk());

        verify(celestialEventService)
                .getCelestialEventsByMonthId(CalendarTestConstants.MONTH_ID);
    }

    //GET by YearId
    @Test
    void getCelestialEventsByYearId_returnsCelestialEvents() throws Exception {

        when(celestialEventService.getCelestialEventsByYearId(CalendarTestConstants.CELESTIAL_EVENT_YEAR))
                .thenReturn(List.of(CalendarTestDataFactory.viewCelestialEventDTO()));

        get("/api/celestialevents/year/" + CalendarTestConstants.CELESTIAL_EVENT_YEAR)
                .andExpect(status().isOk());

        verify(celestialEventService)
                .getCelestialEventsByYearId(CalendarTestConstants.CELESTIAL_EVENT_YEAR);
    }

    //GET by CampaignUUID
    @Test
    void getCelestialEventsByCampaignUUID_returnsCelestialEvents() throws Exception {

        when(celestialEventService.getCelestialEventsByCampaignUUID(CalendarTestConstants.CAMPAIGN_UUID))
                .thenReturn(List.of(CalendarTestDataFactory.viewCelestialEventDTO()));

        get("/api/celestialevents/campaign/" + CalendarTestConstants.CAMPAIGN_UUID)
                .andExpect(status().isOk());

        verify(celestialEventService)
                .getCelestialEventsByCampaignUUID(CalendarTestConstants.CAMPAIGN_UUID);
    }

    //POST
    @Test
    void createCelestialEvent_returnsSaved() throws Exception {

        CreateCelestialEventDTO create =
                CalendarTestDataFactory.createCelestialEventDTO();

        ViewCelestialEventDTO response =
                CalendarTestDataFactory.viewCelestialEventDTO();

        when(celestialEventService.create(any())).thenReturn(response);

        post("/api/celestialevents", create)
                .andExpect(status().isOk());
    }

    //PUT
    @Test
    void updateCelestialEvent_returnsUpdated() throws Exception {

        UpdateCelestialEventDTO update =
                CalendarTestDataFactory.updateCelestialEventDTO();

        ViewCelestialEventDTO response =
                CalendarTestDataFactory.viewCelestialEventDTO();

        when(celestialEventService.update(any())).thenReturn(response);

        put("/api/celestialevents", update)
                .andExpect(status().isOk());
    }

    //DELETE
    @Test
    void deleteCelestialEvent_returnsOk() throws Exception {

        delete("/api/celestialevents/1")
                .andExpect(status().isOk());

        verify(celestialEventService).delete(1);
    }
}