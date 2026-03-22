package com.mcommings.campaigner.setup.calendar.factories;

import com.mcommings.campaigner.modules.calendar.dtos.celestial_events.CreateCelestialEventDTO;
import com.mcommings.campaigner.modules.calendar.dtos.celestial_events.UpdateCelestialEventDTO;
import com.mcommings.campaigner.modules.calendar.dtos.celestial_events.ViewCelestialEventDTO;
import com.mcommings.campaigner.modules.calendar.dtos.days.CreateDayDTO;
import com.mcommings.campaigner.modules.calendar.dtos.days.UpdateDayDTO;
import com.mcommings.campaigner.modules.calendar.dtos.days.ViewDayDTO;
import com.mcommings.campaigner.modules.calendar.entities.CelestialEvent;
import com.mcommings.campaigner.modules.calendar.entities.Day;
import com.mcommings.campaigner.setup.calendar.builders.CelestialEventBuilder;
import com.mcommings.campaigner.setup.calendar.builders.DayBuilder;
import com.mcommings.campaigner.setup.calendar.fixtures.CalendarTestConstants;

public class CalendarTestDataFactory {

    //CELESTIAL EVENTS
    public static CelestialEvent celestialEvent() {
        return CelestialEventBuilder.aCelestialEvent().build();
    }

    public static ViewCelestialEventDTO viewCelestialEventDTO() {
        ViewCelestialEventDTO dto = new ViewCelestialEventDTO();
        dto.setId(CalendarTestConstants.CELESTIAL_EVENT_ID);
        dto.setName(CalendarTestConstants.CELESTIAL_EVENT_NAME);
        dto.setDescription(CalendarTestConstants.CELESTIAL_EVENT_DESCRIPTION);
        dto.setCampaignUuid(CalendarTestConstants.CAMPAIGN_UUID);
        dto.setMoonId(CalendarTestConstants.MOON_ID);
        dto.setSunId(CalendarTestConstants.SUN_ID);
        dto.setMonthId(CalendarTestConstants.MONTH_ID);
        dto.setWeekId(CalendarTestConstants.WEEK_ID);
        dto.setDayId(CalendarTestConstants.DAY_ID);
        dto.setYear(CalendarTestConstants.CELESTIAL_EVENT_YEAR);
        return dto;
    }

    public static CreateCelestialEventDTO createCelestialEventDTO() {
        CreateCelestialEventDTO dto = new CreateCelestialEventDTO();
        dto.setName(CalendarTestConstants.CELESTIAL_EVENT_NAME);
        dto.setDescription(CalendarTestConstants.CELESTIAL_EVENT_DESCRIPTION);
        dto.setCampaignUuid(CalendarTestConstants.CAMPAIGN_UUID);
        dto.setMoonId(CalendarTestConstants.MOON_ID);
        dto.setSunId(CalendarTestConstants.SUN_ID);
        dto.setMonthId(CalendarTestConstants.MONTH_ID);
        dto.setWeekId(CalendarTestConstants.WEEK_ID);
        dto.setDayId(CalendarTestConstants.DAY_ID);
        dto.setYear(CalendarTestConstants.CELESTIAL_EVENT_YEAR);
        return dto;
    }

    public static UpdateCelestialEventDTO updateCelestialEventDTO() {
        UpdateCelestialEventDTO dto = new UpdateCelestialEventDTO();
        dto.setId(CalendarTestConstants.CELESTIAL_EVENT_ID);
        dto.setName(CalendarTestConstants.CELESTIAL_EVENT_NAME);
        dto.setDescription(CalendarTestConstants.CELESTIAL_EVENT_DESCRIPTION);
        dto.setCampaignUuid(CalendarTestConstants.CAMPAIGN_UUID);
        return dto;
    }

    //DAYS
    public static Day day() {
        return DayBuilder.aDay().build();
    }

    public static ViewDayDTO viewDayDTO() {
        ViewDayDTO dto = new ViewDayDTO();
        dto.setId(CalendarTestConstants.DAY_ID);
        dto.setName(CalendarTestConstants.DAY_NAME);
        dto.setDescription(CalendarTestConstants.DAY_DESCRIPTION);
        dto.setCampaignUuid(CalendarTestConstants.CAMPAIGN_UUID);
        dto.setWeekId(CalendarTestConstants.WEEK_ID);
        return dto;
    }

    public static CreateDayDTO createDayDTO() {
        CreateDayDTO dto = new CreateDayDTO();
        dto.setName(CalendarTestConstants.DAY_NAME);
        dto.setDescription(CalendarTestConstants.DAY_DESCRIPTION);
        dto.setCampaignUuid(CalendarTestConstants.CAMPAIGN_UUID);
        dto.setWeekId(CalendarTestConstants.WEEK_ID);
        return dto;
    }

    public static UpdateDayDTO updateDayDTO() {
        UpdateDayDTO dto = new UpdateDayDTO();
        dto.setId(CalendarTestConstants.DAY_ID);
        dto.setName(CalendarTestConstants.DAY_NAME);
        dto.setDescription(CalendarTestConstants.DAY_DESCRIPTION);
        dto.setCampaignUuid(CalendarTestConstants.CAMPAIGN_UUID);
        return dto;
    }
}
