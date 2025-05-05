package com.mcommings.campaigner.modules.common.services.interfaces;

import com.mcommings.campaigner.modules.common.dtos.CampaignDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ICampaign {

    List<CampaignDTO> getCampaigns();

    Optional<CampaignDTO> getCampaign(UUID uuid);

    void saveCampaign(CampaignDTO campaign);

    void deleteCampaign(UUID uuid);

    Optional<CampaignDTO> updateCampaign(UUID uuid, CampaignDTO campaign);
}
