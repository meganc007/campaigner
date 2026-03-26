package com.mcommings.campaigner.setup.calendar.builders;

import com.mcommings.campaigner.modules.calendar.entities.Sun;
import com.mcommings.campaigner.modules.common.entities.Campaign;
import com.mcommings.campaigner.setup.calendar.fixtures.CalendarTestConstants;

import java.util.UUID;

public class SunBuilder {

    private int id = CalendarTestConstants.SUN_ID;
    private String name = CalendarTestConstants.SUN_NAME;
    private String description = CalendarTestConstants.SUN_DESCRIPTION;
    private UUID campaignUuid = CalendarTestConstants.CAMPAIGN_UUID;

    public static SunBuilder aSun() {
        return new SunBuilder();
    }

    public SunBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public SunBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public SunBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public SunBuilder withCampaignUuid(UUID uuid) {
        this.campaignUuid = uuid;
        return this;
    }

    public Sun build() {
        Sun sun = new Sun();
        sun.setId(id);
        sun.setName(name);
        sun.setDescription(description);

        Campaign campaign = new Campaign();
        campaign.setUuid(campaignUuid);
        sun.setCampaign(campaign);

        return sun;
    }
}
