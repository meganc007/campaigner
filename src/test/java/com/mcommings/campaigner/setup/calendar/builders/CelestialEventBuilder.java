package com.mcommings.campaigner.setup.calendar.builders;

import com.mcommings.campaigner.modules.calendar.entities.*;
import com.mcommings.campaigner.modules.common.entities.Campaign;
import com.mcommings.campaigner.setup.calendar.fixtures.CalendarTestConstants;

import java.util.UUID;

public class CelestialEventBuilder {

    private int id = CalendarTestConstants.CELESTIAL_EVENT_ID;
    private String name = CalendarTestConstants.CELESTIAL_EVENT_NAME;
    private String description = CalendarTestConstants.CELESTIAL_EVENT_DESCRIPTION;
    private UUID campaignUuid = CalendarTestConstants.CAMPAIGN_UUID;
    private int moonId = CalendarTestConstants.MOON_ID;
    private int sunId = CalendarTestConstants.SUN_ID;
    private int monthId = CalendarTestConstants.MONTH_ID;
    private int weekId = CalendarTestConstants.WEEK_ID;
    private int dayId = CalendarTestConstants.DAY_ID;
    private int yearId = CalendarTestConstants.CELESTIAL_EVENT_YEAR;

    public static CelestialEventBuilder aCelestialEvent() {
        return new CelestialEventBuilder();
    }

    public CelestialEventBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public CelestialEventBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public CelestialEventBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public CelestialEventBuilder withCampaignUuid(UUID uuid) {
        this.campaignUuid = uuid;
        return this;
    }

    public CelestialEventBuilder withMoonId(int moonId) {
        this.moonId = moonId;
        return this;
    }

    public CelestialEventBuilder withSunId(int sunId) {
        this.sunId = sunId;
        return this;
    }

    public CelestialEventBuilder withMonthId(int monthId) {
        this.monthId = monthId;
        return this;
    }

    public CelestialEventBuilder withWeekId(int weekId) {
        this.weekId = weekId;
        return this;
    }

    public CelestialEventBuilder withDayId(int dayId) {
        this.dayId = dayId;
        return this;
    }

    public CelestialEventBuilder withYearId(int yearId) {
        this.yearId = yearId;
        return this;
    }

    public CelestialEvent build() {
        CelestialEvent celestialEvent = new CelestialEvent();
        celestialEvent.setId(id);
        celestialEvent.setName(name);
        celestialEvent.setDescription(description);

        Campaign campaign = new Campaign();
        campaign.setUuid(campaignUuid);
        celestialEvent.setCampaign(campaign);

        Moon moon = new Moon();
        moon.setId(moonId);
        celestialEvent.setMoon(moon);

        Sun sun = new Sun();
        sun.setId(sunId);
        celestialEvent.setSun(sun);

        Month month = new Month();
        month.setId(monthId);
        celestialEvent.setMonth(month);

        Week week = new Week();
        week.setId(weekId);
        celestialEvent.setWeek(week);

        Day day = new Day();
        day.setId(dayId);
        celestialEvent.setDay(day);

        celestialEvent.setYear(yearId);

        return celestialEvent;
    }
}
