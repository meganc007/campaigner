package com.mcommings.campaigner.calendar.services.interfaces;

import com.mcommings.campaigner.calendar.entities.Month;

import java.util.List;
import java.util.UUID;

public interface IMonth {

    List<Month> getMonths();

    List<Month> getMonthsByCampaignUUID(UUID uuid);

    void saveMonth(Month month);

    void deleteMonth(int monthId);

    void updateMonth(int monthId, Month month);

}
