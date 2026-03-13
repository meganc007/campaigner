package com.mcommings.campaigner.setup.locations.builders;

import com.mcommings.campaigner.modules.common.entities.Campaign;
import com.mcommings.campaigner.modules.common.entities.Climate;
import com.mcommings.campaigner.modules.locations.entities.Country;
import com.mcommings.campaigner.modules.locations.entities.Region;
import com.mcommings.campaigner.setup.locations.fixtures.LocationsTestConstants;

import java.util.UUID;

public class RegionBuilder {
    private int id = LocationsTestConstants.REGION_ID;
    private String name = LocationsTestConstants.REGION_NAME;
    private String description = LocationsTestConstants.REGION_DESCRIPTION;
    private UUID campaignUuid = LocationsTestConstants.CAMPAIGN_UUID;
    private Integer climateId = LocationsTestConstants.CLIMATE_ID;
    private Integer countryId = LocationsTestConstants.COUNTRY_ID;

    public static RegionBuilder aRegion() {
        return new RegionBuilder();
    }

    public RegionBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public RegionBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public RegionBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public RegionBuilder withCampaignUuid(UUID uuid) {
        this.campaignUuid = uuid;
        return this;
    }

    public RegionBuilder withClimateId(int climateId) {
        this.climateId = climateId;
        return this;
    }

    public RegionBuilder withCountryId(int countryId) {
        this.countryId = countryId;
        return this;
    }

    public Region build() {
        Region region = new Region();
        region.setId(id);
        region.setName(name);
        region.setDescription(description);

        Campaign campaign = new Campaign();
        campaign.setUuid(campaignUuid);
        region.setCampaign(campaign);

        Climate climate = new Climate();
        climate.setId(climateId);
        region.setClimate(climate);

        Country country = new Country();
        country.setId(countryId);
        region.setCountry(country);

        return region;
    }
}
