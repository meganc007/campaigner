package com.mcommings.campaigner.setup.common.builders;

import com.mcommings.campaigner.modules.common.entities.Campaign;
import com.mcommings.campaigner.setup.common.fixtures.CommonTestConstants;

import java.util.UUID;

public class CampaignBuilder {

    private UUID uuid = CommonTestConstants.CAMPAIGN_UUID;
    private String name = CommonTestConstants.CAMPAIGN_NAME;
    private String description = CommonTestConstants.CAMPAIGN_DESCRIPTION;

    public static CampaignBuilder aCampaign() {
        return new CampaignBuilder();
    }

    public CampaignBuilder withUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public CampaignBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public CampaignBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public Campaign build() {
        Campaign campaign = new Campaign();
        campaign.setUuid(uuid);
        campaign.setName(name);
        campaign.setDescription(description);
        return campaign;
    }
}
