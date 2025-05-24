package com.mcommings.campaigner.modules.overview.services.interfaces;

import com.mcommings.campaigner.modules.overview.dtos.LocationOverviewDTO;

import java.util.UUID;

public interface ILocationOverview {

    LocationOverviewDTO getLocationOverview(UUID campaignId);
}
