package com.mcommings.campaigner.setup.calendar.builders;

import com.mcommings.campaigner.modules.calendar.entities.Day;
import com.mcommings.campaigner.modules.calendar.entities.Event;
import com.mcommings.campaigner.modules.calendar.entities.Month;
import com.mcommings.campaigner.modules.calendar.entities.Week;
import com.mcommings.campaigner.modules.common.entities.Campaign;
import com.mcommings.campaigner.modules.locations.entities.City;
import com.mcommings.campaigner.modules.locations.entities.Continent;
import com.mcommings.campaigner.modules.locations.entities.Country;
import com.mcommings.campaigner.setup.calendar.fixtures.CalendarTestConstants;
import com.mcommings.campaigner.setup.locations.fixtures.LocationsTestConstants;

import java.util.UUID;

public class EventBuilder {

    private int id = CalendarTestConstants.EVENT_ID;
    private String name = CalendarTestConstants.EVENT_NAME;
    private String description = CalendarTestConstants.EVENT_DESCRIPTION;
    private UUID campaignUuid = CalendarTestConstants.CAMPAIGN_UUID;

    private int monthId = CalendarTestConstants.MONTH_ID;
    private int weekId = CalendarTestConstants.WEEK_ID;
    private int dayId = CalendarTestConstants.DAY_ID;
    private int yearId = CalendarTestConstants.EVENT_YEAR;
    private int continentId = LocationsTestConstants.CONTINENT_ID;
    private int countryId = LocationsTestConstants.COUNTRY_ID;
    private int cityId = LocationsTestConstants.CITY_ID;

    public static EventBuilder aEvent() {
        return new EventBuilder();
    }

    public EventBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public EventBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public EventBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public EventBuilder withCampaignUuid(UUID uuid) {
        this.campaignUuid = uuid;
        return this;
    }

    public EventBuilder withMonthId(int monthId) {
        this.monthId = monthId;
        return this;
    }

    public EventBuilder withWeekId(int weekId) {
        this.weekId = weekId;
        return this;
    }

    public EventBuilder withDayId(int dayId) {
        this.dayId = dayId;
        return this;
    }

    public EventBuilder withYearId(int yearId) {
        this.yearId = yearId;
        return this;
    }

    public EventBuilder withContinentId(int continentId) {
        this.continentId = continentId;
        return this;
    }

    public EventBuilder withCountryId(int countryId) {
        this.countryId = countryId;
        return this;
    }

    public EventBuilder withCityId(int cityId) {
        this.cityId = cityId;
        return this;
    }

    public Event build() {
        Event Event = new Event();
        Event.setId(id);
        Event.setName(name);
        Event.setDescription(description);

        Campaign campaign = new Campaign();
        campaign.setUuid(campaignUuid);
        Event.setCampaign(campaign);

        Month month = new Month();
        month.setId(monthId);
        Event.setMonth(month);

        Week week = new Week();
        week.setId(weekId);
        Event.setWeek(week);

        Day day = new Day();
        day.setId(dayId);
        Event.setDay(day);

        Event.setYear(yearId);

        Continent continent = new Continent();
        continent.setId(continentId);
        Event.setContinent(continent);

        Country country = new Country();
        country.setId(countryId);
        Event.setCountry(country);

        City city = new City();
        city.setId(cityId);
        Event.setCity(city);

        return Event;
    }
}
