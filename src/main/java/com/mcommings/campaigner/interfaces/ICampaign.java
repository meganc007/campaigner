package com.mcommings.campaigner.interfaces;

import com.mcommings.campaigner.entities.Campaign;

import java.util.List;
import java.util.UUID;

public interface ICampaign {

    List<Campaign> getCampaigns();

    Campaign getCampaign(UUID uuid);

    void saveCampaign(Campaign campaign);

    void deleteCampaign(UUID uuid);

    void updateCampaign(UUID uuid, Campaign campaign);
}
