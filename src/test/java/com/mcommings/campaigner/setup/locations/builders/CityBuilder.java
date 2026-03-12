package com.mcommings.campaigner.setup.locations.builders;

import com.mcommings.campaigner.modules.common.entities.Campaign;
import com.mcommings.campaigner.modules.common.entities.Government;
import com.mcommings.campaigner.modules.common.entities.Wealth;
import com.mcommings.campaigner.modules.locations.entities.City;
import com.mcommings.campaigner.modules.locations.entities.Country;
import com.mcommings.campaigner.modules.locations.entities.Region;
import com.mcommings.campaigner.modules.locations.entities.SettlementType;
import com.mcommings.campaigner.setup.locations.fixtures.LocationsTestConstants;

import java.util.UUID;

public class CityBuilder {

    private int id = LocationsTestConstants.CITY_ID;
    private String name = LocationsTestConstants.CITY_NAME;
    private String description = LocationsTestConstants.CITY_DESCRIPTION;
    private UUID campaignUuid = LocationsTestConstants.CAMPAIGN_UUID;
    private Integer wealthId = LocationsTestConstants.WEALTH_ID;
    private Integer countryId = LocationsTestConstants.COUNTRY_ID;
    private Integer settlementTypeId = LocationsTestConstants.SETTLEMENTTYPE_ID;
    private Integer governmentId = LocationsTestConstants.GOVERNMENT_ID;
    private Integer regionId = LocationsTestConstants.REGION_ID;

    public static CityBuilder aCity() {
        return new CityBuilder();
    }

    public CityBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public CityBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public CityBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public CityBuilder withCampaignUuid(UUID uuid) {
        this.campaignUuid = uuid;
        return this;
    }

    public CityBuilder withWealthId(int wealthId) {
        this.wealthId = wealthId;
        return this;
    }

    public CityBuilder withCountryId(int countryId) {
        this.countryId = countryId;
        return this;
    }

    public CityBuilder withSettlementTypeId(int settlementTypeId) {
        this.settlementTypeId = settlementTypeId;
        return this;
    }

    public CityBuilder withGovernmentId(int governmentId) {
        this.governmentId = governmentId;
        return this;
    }

    public CityBuilder withRegionId(int regionId) {
        this.regionId = regionId;
        return this;
    }

    public City build() {
        City city = new City();
        city.setId(id);
        city.setName(name);
        city.setDescription(description);

        Campaign campaign = new Campaign();
        campaign.setUuid(campaignUuid);
        city.setCampaign(campaign);

        Wealth wealth = new Wealth();
        wealth.setId(wealthId);
        city.setWealth(wealth);

        Country country = new Country();
        country.setId(countryId);
        city.setCountry(country);

        SettlementType settlementType = new SettlementType();
        settlementType.setId(settlementTypeId);
        city.setSettlementType(settlementType);


        Government government = new Government();
        government.setId(governmentId);
        city.setGovernment(government);

        Region region = new Region();
        region.setId(regionId);
        city.setRegion(region);

        return city;
    }
}
