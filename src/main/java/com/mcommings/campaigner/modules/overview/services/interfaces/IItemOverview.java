package com.mcommings.campaigner.modules.overview.services.interfaces;

import com.mcommings.campaigner.modules.overview.dtos.ItemOverviewDTO;
import com.mcommings.campaigner.modules.overview.helpers.InventoryOverview;

import java.util.List;
import java.util.UUID;

public interface IItemOverview {

    ItemOverviewDTO getItemOverview(UUID campaignId);

    List<InventoryOverview> getInventoryOverview(UUID campaignId);
}
