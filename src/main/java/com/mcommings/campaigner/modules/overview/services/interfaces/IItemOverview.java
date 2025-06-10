package com.mcommings.campaigner.modules.overview.services.interfaces;

import com.mcommings.campaigner.modules.overview.dtos.ItemOverviewDTO;

import java.util.UUID;

public interface IItemOverview {

    ItemOverviewDTO getItemOverview(UUID campaignId);
}
