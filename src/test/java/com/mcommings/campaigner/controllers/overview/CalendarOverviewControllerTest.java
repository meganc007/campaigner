package com.mcommings.campaigner.controllers.overview;

import com.mcommings.campaigner.modules.calendar.dtos.SunDTO;
import com.mcommings.campaigner.modules.overview.controllers.CalendarOverviewController;
import com.mcommings.campaigner.modules.overview.dtos.CalendarOverviewDTO;
import com.mcommings.campaigner.modules.overview.services.CalendarOverviewService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CalendarOverviewController.class)
public class CalendarOverviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CalendarOverviewService calendarOverviewService;

    private final String URI = "/api/calendar-overview/";

    @Test
    void whenCampaignUUIDIsValid_getCalendarOverview_returnsOverviewDataAsJson() throws Exception {
        UUID campaignId = UUID.randomUUID();

        CalendarOverviewDTO dto = CalendarOverviewDTO.builder()
                .suns(List.of(new SunDTO(1, "Sol", "this is a sun", campaignId)))
                .moons(Collections.emptyList())
                .months(Collections.emptyList())
                .weeks(Collections.emptyList())
                .days(Collections.emptyList())
                .celestialEvents(Collections.emptyList())
                .events(Collections.emptyList())
                .build();

        when(calendarOverviewService.getCalendarOverview(campaignId)).thenReturn(dto);

        mockMvc.perform(get(URI + campaignId).param("campaignId", campaignId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.suns[0].name").value("Sol"));

    }

    @Test
    void whenCampaignUUIDIsInvalid_thenReturnEmptyOverview() throws Exception {
        UUID invalidCampaignId = UUID.randomUUID();

        CalendarOverviewDTO emptyOverview = CalendarOverviewDTO.builder()
                .suns(Collections.emptyList())
                .moons(Collections.emptyList())
                .months(Collections.emptyList())
                .weeks(Collections.emptyList())
                .days(Collections.emptyList())
                .celestialEvents(Collections.emptyList())
                .events(Collections.emptyList())
                .build();

        when(calendarOverviewService.getCalendarOverview(invalidCampaignId)).thenReturn(emptyOverview);

        mockMvc.perform(get(URI + invalidCampaignId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.suns").isArray())
                .andExpect(jsonPath("$.suns").isEmpty())
                .andExpect(jsonPath("$.moons").isArray())
                .andExpect(jsonPath("$.moons").isEmpty())
                .andExpect(jsonPath("$.months").isArray())
                .andExpect(jsonPath("$.months").isEmpty())
                .andExpect(jsonPath("$.weeks").isArray())
                .andExpect(jsonPath("$.weeks").isEmpty())
                .andExpect(jsonPath("$.days").isArray())
                .andExpect(jsonPath("$.days").isEmpty())
                .andExpect(jsonPath("$.celestialEvents").isArray())
                .andExpect(jsonPath("$.celestialEvents").isEmpty())
                .andExpect(jsonPath("$.events").isArray())
                .andExpect(jsonPath("$.events").isEmpty());

        verify(calendarOverviewService).getCalendarOverview(invalidCampaignId);
    }
}
