package com.mcommings.campaigner.setup.locations.builders;

import com.mcommings.campaigner.modules.common.entities.Campaign;
import com.mcommings.campaigner.modules.locations.entities.Landmark;
import com.mcommings.campaigner.modules.locations.entities.Region;
import com.mcommings.campaigner.setup.locations.fixtures.LocationsTestConstants;

import java.util.UUID;

public class LandmarkBuilder {

    private int id = LocationsTestConstants.LANDMARK_ID;
    private String name = LocationsTestConstants.LANDMARK_NAME;
    private String description = LocationsTestConstants.LANDMARK_DESCRIPTION;
    private UUID campaignUuid = LocationsTestConstants.CAMPAIGN_UUID;
    private Integer regionId = LocationsTestConstants.REGION_ID;

    public static LandmarkBuilder aLandmark() {
        return new LandmarkBuilder();
    }

    public LandmarkBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public LandmarkBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public LandmarkBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public LandmarkBuilder withCampaignUuid(UUID uuid) {
        this.campaignUuid = uuid;
        return this;
    }

    public LandmarkBuilder withRegionId(int regionId) {
        this.regionId = regionId;
        return this;
    }

    public Landmark build() {
        Landmark landmark = new Landmark();
        landmark.setId(id);
        landmark.setName(name);
        landmark.setDescription(description);

        Campaign campaign = new Campaign();
        campaign.setUuid(campaignUuid);
        landmark.setCampaign(campaign);

        Region region = new Region();
        region.setId(regionId);
        landmark.setRegion(region);

        return landmark;
    }
}
