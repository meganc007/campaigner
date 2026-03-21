package com.mcommings.campaigner.setup.common.factories;

import com.mcommings.campaigner.modules.common.dtos.campaigns.CreateCampaignDTO;
import com.mcommings.campaigner.modules.common.dtos.campaigns.UpdateCampaignDTO;
import com.mcommings.campaigner.modules.common.dtos.campaigns.ViewCampaignDTO;
import com.mcommings.campaigner.modules.common.dtos.climate.CreateClimateDTO;
import com.mcommings.campaigner.modules.common.dtos.climate.UpdateClimateDTO;
import com.mcommings.campaigner.modules.common.dtos.climate.ViewClimateDTO;
import com.mcommings.campaigner.modules.common.dtos.government.CreateGovernmentDTO;
import com.mcommings.campaigner.modules.common.dtos.government.UpdateGovernmentDTO;
import com.mcommings.campaigner.modules.common.dtos.government.ViewGovernmentDTO;
import com.mcommings.campaigner.modules.common.dtos.wealth.CreateWealthDTO;
import com.mcommings.campaigner.modules.common.dtos.wealth.UpdateWealthDTO;
import com.mcommings.campaigner.modules.common.dtos.wealth.ViewWealthDTO;
import com.mcommings.campaigner.modules.common.entities.Campaign;
import com.mcommings.campaigner.modules.common.entities.Climate;
import com.mcommings.campaigner.modules.common.entities.Government;
import com.mcommings.campaigner.modules.common.entities.Wealth;
import com.mcommings.campaigner.setup.common.builders.CampaignBuilder;
import com.mcommings.campaigner.setup.common.builders.ClimateBuilder;
import com.mcommings.campaigner.setup.common.builders.GovernmentBuilder;
import com.mcommings.campaigner.setup.common.builders.WealthBuilder;
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

    //CLIMATES
    public static Climate climate() {
        return ClimateBuilder.aClimate().build();
    }

    public static ViewClimateDTO viewClimateDTO() {
        ViewClimateDTO dto = new ViewClimateDTO();
        dto.setId(CommonTestConstants.CLIMATE_ID);
        dto.setName(CommonTestConstants.CLIMATE_NAME);
        dto.setDescription(CommonTestConstants.CLIMATE_DESCRIPTION);
        return dto;
    }

    public static CreateClimateDTO createClimateDTO() {
        CreateClimateDTO dto = new CreateClimateDTO();
        dto.setName(CommonTestConstants.CLIMATE_NAME);
        dto.setDescription(CommonTestConstants.CLIMATE_DESCRIPTION);
        return dto;
    }

    public static UpdateClimateDTO updateClimateDTO() {
        UpdateClimateDTO dto = new UpdateClimateDTO();
        dto.setId(CommonTestConstants.CLIMATE_ID);
        dto.setName(CommonTestConstants.CLIMATE_NAME);
        dto.setDescription(CommonTestConstants.CLIMATE_DESCRIPTION);
        return dto;
    }

    //GOVERNMENT
    public static Government government() {
        return GovernmentBuilder.aGovernment().build();
    }

    public static ViewGovernmentDTO viewGovernmentDTO() {
        ViewGovernmentDTO dto = new ViewGovernmentDTO();
        dto.setId(CommonTestConstants.GOVERNMENT_ID);
        dto.setName(CommonTestConstants.GOVERNMENT_NAME);
        dto.setDescription(CommonTestConstants.GOVERNMENT_DESCRIPTION);
        return dto;
    }

    public static CreateGovernmentDTO createGovernmentDTO() {
        CreateGovernmentDTO dto = new CreateGovernmentDTO();
        dto.setName(CommonTestConstants.GOVERNMENT_NAME);
        dto.setDescription(CommonTestConstants.GOVERNMENT_DESCRIPTION);
        return dto;
    }

    public static UpdateGovernmentDTO updateGovernmentDTO() {
        UpdateGovernmentDTO dto = new UpdateGovernmentDTO();
        dto.setId(CommonTestConstants.GOVERNMENT_ID);
        dto.setName(CommonTestConstants.GOVERNMENT_NAME);
        dto.setDescription(CommonTestConstants.GOVERNMENT_DESCRIPTION);
        return dto;
    }

    //WEALTH
    public static Wealth wealth() {
        return WealthBuilder.aWealth().build();
    }

    public static ViewWealthDTO viewWealthDTO() {
        ViewWealthDTO dto = new ViewWealthDTO();
        dto.setId(CommonTestConstants.WEALTH_ID);
        dto.setName(CommonTestConstants.WEALTH_NAME);
        return dto;
    }

    public static CreateWealthDTO createWealthDTO() {
        CreateWealthDTO dto = new CreateWealthDTO();
        dto.setName(CommonTestConstants.WEALTH_NAME);
        return dto;
    }

    public static UpdateWealthDTO updateWealthDTO() {
        UpdateWealthDTO dto = new UpdateWealthDTO();
        dto.setId(CommonTestConstants.WEALTH_ID);
        dto.setName(CommonTestConstants.WEALTH_NAME);
        return dto;
    }
}
