package com.mcommings.campaigner.services.overview;

import com.mcommings.campaigner.modules.calendar.dtos.SunDTO;
import com.mcommings.campaigner.modules.calendar.entities.Sun;
import com.mcommings.campaigner.modules.overview.dtos.CalendarOverviewDTO;
import com.mcommings.campaigner.modules.overview.facades.mappers.CalendarMapperFacade;
import com.mcommings.campaigner.modules.overview.facades.repositories.CalendarRepositoryFacade;
import com.mcommings.campaigner.modules.overview.services.CalendarOverviewService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CalendarOverviewTest {

    @Mock
    private CalendarRepositoryFacade calendarRepositoryFacade;

    @Mock
    private CalendarMapperFacade calendarMapperFacade;

    @InjectMocks
    private CalendarOverviewService calendarOverviewService;

    @Test
    void whenCampaignUUIDIsValid_getCalendarOverview_returnsAggregatedCalendarData() {
        UUID campaignId = UUID.randomUUID();

        Sun sun = new Sun(1, "Sol", "this is a sun", campaignId);
        SunDTO sunDTO = new SunDTO(1, "Sol", "this is a sun", campaignId);

        when(calendarRepositoryFacade.findSuns(campaignId)).thenReturn(List.of(sun));
        when(calendarMapperFacade.toSunDto(sun)).thenReturn(sunDTO);

        CalendarOverviewDTO result = calendarOverviewService.getCalendarOverview(campaignId);

        assertNotNull(result);
        assertEquals(1, result.getSuns().size());
        assertEquals("Sol", result.getSuns().get(0).getName());

        verify(calendarRepositoryFacade).findSuns(campaignId);
        verify(calendarMapperFacade).toSunDto(sun);
    }

    @Test
    void whenCampaignUUIDIsInvalid_getCalendarOverview_returnsEmptyList() {
        UUID invalidCampaignId = UUID.randomUUID();

        when(calendarRepositoryFacade.findSuns(invalidCampaignId)).thenReturn(Collections.emptyList());
        when(calendarRepositoryFacade.findMoons(invalidCampaignId)).thenReturn(Collections.emptyList());
        when(calendarRepositoryFacade.findMonths(invalidCampaignId)).thenReturn(Collections.emptyList());
        when(calendarRepositoryFacade.findWeeks(invalidCampaignId)).thenReturn(Collections.emptyList());
        when(calendarRepositoryFacade.findDays(invalidCampaignId)).thenReturn(Collections.emptyList());
        when(calendarRepositoryFacade.findCelestialEvents(invalidCampaignId)).thenReturn(Collections.emptyList());
        when(calendarRepositoryFacade.findEvents(invalidCampaignId)).thenReturn(Collections.emptyList());

        CalendarOverviewDTO result = calendarOverviewService.getCalendarOverview(invalidCampaignId);

        assertNotNull(result);

        assertTrue(result.getSuns().isEmpty());
        assertTrue(result.getMoons().isEmpty());
        assertTrue(result.getMonths().isEmpty());
        assertTrue(result.getWeeks().isEmpty());
        assertTrue(result.getDays().isEmpty());
        assertTrue(result.getCelestialEvents().isEmpty());
        assertTrue(result.getEvents().isEmpty());

        verify(calendarRepositoryFacade).findSuns(invalidCampaignId);
        verify(calendarRepositoryFacade).findMoons(invalidCampaignId);
        verify(calendarRepositoryFacade).findMonths(invalidCampaignId);
        verify(calendarRepositoryFacade).findWeeks(invalidCampaignId);
        verify(calendarRepositoryFacade).findDays(invalidCampaignId);
        verify(calendarRepositoryFacade).findCelestialEvents(invalidCampaignId);
        verify(calendarRepositoryFacade).findEvents(invalidCampaignId);
    }
}
