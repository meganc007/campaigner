package com.mcommings.campaigner.setup.calendar.factories;

import com.mcommings.campaigner.modules.calendar.dtos.celestial_events.CreateCelestialEventDTO;
import com.mcommings.campaigner.modules.calendar.dtos.celestial_events.UpdateCelestialEventDTO;
import com.mcommings.campaigner.modules.calendar.dtos.celestial_events.ViewCelestialEventDTO;
import com.mcommings.campaigner.modules.calendar.dtos.days.CreateDayDTO;
import com.mcommings.campaigner.modules.calendar.dtos.days.UpdateDayDTO;
import com.mcommings.campaigner.modules.calendar.dtos.days.ViewDayDTO;
import com.mcommings.campaigner.modules.calendar.dtos.events.CreateEventDTO;
import com.mcommings.campaigner.modules.calendar.dtos.events.UpdateEventDTO;
import com.mcommings.campaigner.modules.calendar.dtos.events.ViewEventDTO;
import com.mcommings.campaigner.modules.calendar.dtos.months.CreateMonthDTO;
import com.mcommings.campaigner.modules.calendar.dtos.months.UpdateMonthDTO;
import com.mcommings.campaigner.modules.calendar.dtos.months.ViewMonthDTO;
import com.mcommings.campaigner.modules.calendar.entities.CelestialEvent;
import com.mcommings.campaigner.modules.calendar.entities.Day;
import com.mcommings.campaigner.modules.calendar.entities.Event;
import com.mcommings.campaigner.modules.calendar.entities.Month;
import com.mcommings.campaigner.setup.calendar.builders.CelestialEventBuilder;
import com.mcommings.campaigner.setup.calendar.builders.DayBuilder;
import com.mcommings.campaigner.setup.calendar.builders.EventBuilder;
import com.mcommings.campaigner.setup.calendar.builders.MonthBuilder;
import com.mcommings.campaigner.setup.calendar.fixtures.CalendarTestConstants;
import com.mcommings.campaigner.setup.locations.fixtures.LocationsTestConstants;

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

    //EVENTS
    public static Event event() {
        return EventBuilder.aEvent().build();
    }

    public static ViewEventDTO viewEventDTO() {
        ViewEventDTO dto = new ViewEventDTO();
        dto.setId(CalendarTestConstants.EVENT_ID);
        dto.setName(CalendarTestConstants.EVENT_NAME);
        dto.setDescription(CalendarTestConstants.EVENT_DESCRIPTION);
        dto.setCampaignUuid(CalendarTestConstants.CAMPAIGN_UUID);
        dto.setMonthId(CalendarTestConstants.MONTH_ID);
        dto.setWeekId(CalendarTestConstants.WEEK_ID);
        dto.setDayId(CalendarTestConstants.DAY_ID);
        dto.setContinentId(LocationsTestConstants.CONTINENT_ID);
        dto.setCountryId(LocationsTestConstants.COUNTRY_ID);
        dto.setCityId(LocationsTestConstants.CITY_ID);
        return dto;
    }

    public static CreateEventDTO createEventDTO() {
        CreateEventDTO dto = new CreateEventDTO();
        dto.setName(CalendarTestConstants.EVENT_NAME);
        dto.setDescription(CalendarTestConstants.EVENT_DESCRIPTION);
        dto.setCampaignUuid(CalendarTestConstants.CAMPAIGN_UUID);
        dto.setMonthId(CalendarTestConstants.MONTH_ID);
        dto.setWeekId(CalendarTestConstants.WEEK_ID);
        dto.setDayId(CalendarTestConstants.DAY_ID);
        dto.setYear(CalendarTestConstants.EVENT_YEAR);
        dto.setContinentId(LocationsTestConstants.CONTINENT_ID);
        dto.setCountryId(LocationsTestConstants.COUNTRY_ID);
        dto.setCityId(LocationsTestConstants.CITY_ID);
        return dto;
    }

    public static UpdateEventDTO updateEventDTO() {
        UpdateEventDTO dto = new UpdateEventDTO();
        dto.setId(CalendarTestConstants.EVENT_ID);
        dto.setName(CalendarTestConstants.EVENT_NAME);
        dto.setDescription(CalendarTestConstants.EVENT_DESCRIPTION);
        dto.setCampaignUuid(CalendarTestConstants.CAMPAIGN_UUID);
        return dto;
    }

    //MONTHS
    public static Month month() {
        return MonthBuilder.aMonth().build();
    }

    public static ViewMonthDTO viewMonthDTO() {
        ViewMonthDTO dto = new ViewMonthDTO();
        dto.setId(CalendarTestConstants.MONTH_ID);
        dto.setName(CalendarTestConstants.MONTH_NAME);
        dto.setDescription(CalendarTestConstants.MONTH_DESCRIPTION);
        dto.setCampaignUuid(CalendarTestConstants.CAMPAIGN_UUID);
        dto.setSeason(CalendarTestConstants.MONTH_SEASON);
        return dto;
    }

    public static CreateMonthDTO createMonthDTO() {
        CreateMonthDTO dto = new CreateMonthDTO();
        dto.setName(CalendarTestConstants.MONTH_NAME);
        dto.setDescription(CalendarTestConstants.MONTH_DESCRIPTION);
        dto.setCampaignUuid(CalendarTestConstants.CAMPAIGN_UUID);
        dto.setSeason(CalendarTestConstants.MONTH_SEASON);
        return dto;
    }

    public static UpdateMonthDTO updateMonthDTO() {
        UpdateMonthDTO dto = new UpdateMonthDTO();
        dto.setId(CalendarTestConstants.MONTH_ID);
        dto.setName(CalendarTestConstants.MONTH_NAME);
        dto.setDescription(CalendarTestConstants.MONTH_DESCRIPTION);
        dto.setCampaignUuid(CalendarTestConstants.CAMPAIGN_UUID);
        return dto;
    }
}
