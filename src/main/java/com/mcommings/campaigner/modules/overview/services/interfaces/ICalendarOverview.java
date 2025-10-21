package com.mcommings.campaigner.modules.overview.services.interfaces;

import com.mcommings.campaigner.modules.overview.dtos.CalendarOverviewDTO;

import java.util.UUID;

public interface ICalendarOverview {

    CalendarOverviewDTO getCalendarOverview(UUID campaignId);
}
