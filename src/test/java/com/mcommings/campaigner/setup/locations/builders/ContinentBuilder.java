package com.mcommings.campaigner.setup.locations.builders;

import com.mcommings.campaigner.modules.common.entities.Campaign;
import com.mcommings.campaigner.modules.locations.entities.Continent;
import com.mcommings.campaigner.setup.locations.fixtures.LocationsTestConstants;

import java.util.UUID;

public class ContinentBuilder {

    private int id = LocationsTestConstants.CONTINENT_ID;
    private String name = LocationsTestConstants.CONTINENT_NAME;
    private String description = LocationsTestConstants.CONTINENT_DESCRIPTION;
    private UUID campaignUuid = LocationsTestConstants.CAMPAIGN_UUID;

    public static ContinentBuilder aContinent() {
        return new ContinentBuilder();
    }

    public ContinentBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public ContinentBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ContinentBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public ContinentBuilder withCampaignUuid(UUID uuid) {
        this.campaignUuid = uuid;
        return this;
    }

    public Continent build() {
        Campaign campaign = new Campaign();
        campaign.setUuid(campaignUuid);

        Continent continent = new Continent();
        continent.setId(id);
        continent.setName(name);
        continent.setDescription(description);
        continent.setCampaign(campaign);
        return continent;
    }
}
