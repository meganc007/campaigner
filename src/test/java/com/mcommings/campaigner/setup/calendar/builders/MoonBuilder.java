package com.mcommings.campaigner.setup.calendar.builders;

import com.mcommings.campaigner.modules.calendar.entities.Moon;
import com.mcommings.campaigner.modules.common.entities.Campaign;
import com.mcommings.campaigner.setup.calendar.fixtures.CalendarTestConstants;

import java.util.UUID;

public class MoonBuilder {

    private int id = CalendarTestConstants.MOON_ID;
    private String name = CalendarTestConstants.MOON_NAME;
    private String description = CalendarTestConstants.MOON_DESCRIPTION;
    private UUID campaignUuid = CalendarTestConstants.CAMPAIGN_UUID;

    public static MoonBuilder aMoon() {
        return new MoonBuilder();
    }

    public MoonBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public MoonBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public MoonBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public MoonBuilder withCampaignUuid(UUID uuid) {
        this.campaignUuid = uuid;
        return this;
    }

    public Moon build() {
        Moon moon = new Moon();
        moon.setId(id);
        moon.setName(name);
        moon.setDescription(description);

        Campaign campaign = new Campaign();
        campaign.setUuid(campaignUuid);
        moon.setCampaign(campaign);

        return moon;
    }
}
