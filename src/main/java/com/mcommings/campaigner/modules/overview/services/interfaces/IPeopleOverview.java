package com.mcommings.campaigner.modules.overview.services.interfaces;

import com.mcommings.campaigner.modules.overview.dtos.PeopleOverviewDTO;

import java.util.UUID;

public interface IPeopleOverview {

    PeopleOverviewDTO getPeopleOverview(UUID campaignId);
}
