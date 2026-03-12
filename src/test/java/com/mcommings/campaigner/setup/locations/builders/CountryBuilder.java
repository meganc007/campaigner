package com.mcommings.campaigner.setup.locations.builders;

import com.mcommings.campaigner.modules.common.entities.Campaign;
import com.mcommings.campaigner.modules.common.entities.Government;
import com.mcommings.campaigner.modules.locations.entities.Continent;
import com.mcommings.campaigner.modules.locations.entities.Country;
import com.mcommings.campaigner.setup.locations.fixtures.LocationsTestConstants;

import java.util.UUID;

public class CountryBuilder {

    private int id = LocationsTestConstants.COUNTRY_ID;
    private String name = LocationsTestConstants.COUNTRY_NAME;
    private String description = LocationsTestConstants.COUNTRY_DESCRIPTION;
    private UUID campaignUuid = LocationsTestConstants.CAMPAIGN_UUID;
    private Integer continentId = LocationsTestConstants.CONTINENT_ID;
    private Integer governmentId = LocationsTestConstants.GOVERNMENT_ID;

    public static CountryBuilder aCountry() {
        return new CountryBuilder();
    }

    public CountryBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public CountryBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public CountryBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public CountryBuilder withCampaignUuid(UUID uuid) {
        this.campaignUuid = uuid;
        return this;
    }

    public CountryBuilder withContinentId(int continentId) {
        this.continentId = continentId;
        return this;
    }

    public CountryBuilder withGovernmentId(int governmentId) {
        this.governmentId = governmentId;
        return this;
    }

    public Country build() {
        Country country = new Country();
        country.setId(id);
        country.setName(name);
        country.setDescription(description);

        Campaign campaign = new Campaign();
        campaign.setUuid(campaignUuid);
        country.setCampaign(campaign);

        Continent continent = new Continent();
        continent.setId(continentId);
        country.setContinent(continent);

        Government government = new Government();
        government.setId(governmentId);
        country.setGovernment(government);

        return country;
    }
}
