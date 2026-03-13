package com.mcommings.campaigner.setup.locations.builders;

import com.mcommings.campaigner.modules.common.entities.Campaign;
import com.mcommings.campaigner.modules.locations.entities.*;
import com.mcommings.campaigner.setup.locations.fixtures.LocationsTestConstants;

import java.util.UUID;

public class PlaceBuilder {

    private int id = LocationsTestConstants.PLACE_ID;
    private String name = LocationsTestConstants.PLACE_NAME;
    private String description = LocationsTestConstants.PLACE_DESCRIPTION;
    private UUID campaignUuid = LocationsTestConstants.CAMPAIGN_UUID;
    private Integer placeTypeId = LocationsTestConstants.PLACETYPE_ID;
    private Integer terrainId = LocationsTestConstants.TERRAIN_ID;
    private Integer countryId = LocationsTestConstants.COUNTRY_ID;
    private Integer cityId = LocationsTestConstants.CITY_ID;
    private Integer regionId = LocationsTestConstants.REGION_ID;

    public static PlaceBuilder aPlace() {
        return new PlaceBuilder();
    }

    public PlaceBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public PlaceBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public PlaceBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public PlaceBuilder withCampaignUuid(UUID uuid) {
        this.campaignUuid = uuid;
        return this;
    }

    public PlaceBuilder withPlaceTypeId(int placeTypeId) {
        this.placeTypeId = placeTypeId;
        return this;
    }

    public PlaceBuilder withTerrainId(int terrainId) {
        this.terrainId = terrainId;
        return this;
    }

    public PlaceBuilder withCountryId(int countryId) {
        this.countryId = countryId;
        return this;
    }

    public PlaceBuilder withCityId(int cityId) {
        this.cityId = cityId;
        return this;
    }

    public PlaceBuilder withRegionId(int regionId) {
        this.regionId = regionId;
        return this;
    }

    public Place build() {
        Place place = new Place();
        place.setId(id);
        place.setName(name);
        place.setDescription(description);

        Campaign campaign = new Campaign();
        campaign.setUuid(campaignUuid);
        place.setCampaign(campaign);

        PlaceType placeType = new PlaceType();
        placeType.setId(placeTypeId);
        place.setPlaceType(placeType);

        Terrain terrain = new Terrain();
        terrain.setId(terrainId);
        place.setTerrain(terrain);

        Country country = new Country();
        country.setId(countryId);
        place.setCountry(country);

        City city = new City();
        city.setId(cityId);
        place.setCity(city);

        Region region = new Region();
        region.setId(regionId);
        place.setRegion(region);

        return place;
    }
}
