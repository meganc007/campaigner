package com.mcommings.campaigner.modules.calendar.services.interfaces;

import com.mcommings.campaigner.modules.calendar.dtos.MonthDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IMonth {

    List<MonthDTO> getMonths();

    List<MonthDTO> getMonthsByCampaignUUID(UUID uuid);

    void saveMonth(MonthDTO month);

    void deleteMonth(int monthId);

    Optional<MonthDTO> updateMonth(int monthId, MonthDTO month);

}
