package com.mcommings.campaigner.setup.common.factories;

import com.mcommings.campaigner.modules.common.dtos.campaigns.CreateCampaignDTO;
import com.mcommings.campaigner.modules.common.dtos.campaigns.UpdateCampaignDTO;
import com.mcommings.campaigner.modules.common.dtos.campaigns.ViewCampaignDTO;
import com.mcommings.campaigner.modules.common.entities.Campaign;
import com.mcommings.campaigner.setup.common.builders.CampaignBuilder;
import com.mcommings.campaigner.setup.common.fixtures.CommonTestConstants;

public class CommonTestDataFactory {

    //CAMPAIGNS
    public static Campaign campaign() {
        return CampaignBuilder.aCampaign().build();
    }

    public static ViewCampaignDTO viewCampaignDTO() {
        ViewCampaignDTO dto = new ViewCampaignDTO();
        dto.setUuid(CommonTestConstants.CAMPAIGN_UUID);
        dto.setName(CommonTestConstants.CAMPAIGN_NAME);
        dto.setDescription(CommonTestConstants.CAMPAIGN_DESCRIPTION);
        return dto;
    }

    public static CreateCampaignDTO createCampaignDTO() {
        CreateCampaignDTO dto = new CreateCampaignDTO();
        dto.setName(CommonTestConstants.CAMPAIGN_NAME);
        dto.setDescription(CommonTestConstants.CAMPAIGN_DESCRIPTION);
        return dto;
    }

    public static UpdateCampaignDTO updateCampaignDTO() {
        UpdateCampaignDTO dto = new UpdateCampaignDTO();
        dto.setUuid(CommonTestConstants.CAMPAIGN_UUID);
        dto.setName(CommonTestConstants.CAMPAIGN_NAME);
        dto.setDescription(CommonTestConstants.CAMPAIGN_DESCRIPTION);
        return dto;
    }
}
