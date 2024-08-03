package com.mcommings.campaigner.interfaces.calendar;

import com.mcommings.campaigner.models.calendar.Month;

import java.util.List;
import java.util.UUID;

public interface IMonth {

    List<Month> getMonths();

    List<Month> getMonthsByCampaignUUID(UUID uuid);

    void saveMonth(Month month);

    void deleteMonth(int monthId);

    void updateMonth(int monthId, Month month);

}
