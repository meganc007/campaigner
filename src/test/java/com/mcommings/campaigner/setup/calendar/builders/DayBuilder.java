package com.mcommings.campaigner.setup.calendar.builders;

import com.mcommings.campaigner.modules.calendar.entities.Day;
import com.mcommings.campaigner.modules.calendar.entities.Week;
import com.mcommings.campaigner.modules.common.entities.Campaign;
import com.mcommings.campaigner.setup.calendar.fixtures.CalendarTestConstants;

import java.util.UUID;

public class DayBuilder {

    private int id = CalendarTestConstants.DAY_ID;
    private String name = CalendarTestConstants.DAY_NAME;
    private String description = CalendarTestConstants.DAY_DESCRIPTION;
    private UUID campaignUuid = CalendarTestConstants.CAMPAIGN_UUID;
    private Integer weekId = CalendarTestConstants.WEEK_ID;

    public static DayBuilder aDay() {
        return new DayBuilder();
    }

    public DayBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public DayBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public DayBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public DayBuilder withCampaignUuid(UUID uuid) {
        this.campaignUuid = uuid;
        return this;
    }

    public DayBuilder withWeekId(int weekId) {
        this.weekId = weekId;
        return this;
    }

    public Day build() {
        Day day = new Day();
        day.setId(id);
        day.setName(name);
        day.setDescription(description);

        Campaign campaign = new Campaign();
        campaign.setUuid(campaignUuid);
        day.setCampaign(campaign);

        Week week = new Week();
        week.setId(weekId);
        day.setWeek(week);

        return day;
    }
}
