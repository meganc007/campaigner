package com.mcommings.campaigner.setup.calendar.builders;

import com.mcommings.campaigner.modules.calendar.entities.Month;
import com.mcommings.campaigner.modules.common.entities.Campaign;
import com.mcommings.campaigner.setup.calendar.fixtures.CalendarTestConstants;

import java.util.UUID;

public class MonthBuilder {

    private int id = CalendarTestConstants.MONTH_ID;
    private String name = CalendarTestConstants.MONTH_NAME;
    private String description = CalendarTestConstants.MONTH_DESCRIPTION;
    private UUID campaignUuid = CalendarTestConstants.CAMPAIGN_UUID;
    private String season = CalendarTestConstants.MONTH_SEASON;

    public static MonthBuilder aMonth() {
        return new MonthBuilder();
    }

    public MonthBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public MonthBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public MonthBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public MonthBuilder withCampaignUuid(UUID uuid) {
        this.campaignUuid = uuid;
        return this;
    }

    public MonthBuilder withSeason(String season) {
        this.season = season;
        return this;
    }

    public Month build() {
        Month month = new Month();
        month.setId(id);
        month.setName(name);
        month.setDescription(description);

        Campaign campaign = new Campaign();
        campaign.setUuid(campaignUuid);
        month.setCampaign(campaign);

        month.setSeason(season);

        return month;
    }
}
