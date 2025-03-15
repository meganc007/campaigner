package com.mcommings.campaigner.modules.calendar.services.interfaces;

import com.mcommings.campaigner.modules.calendar.dtos.WeekDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IWeek {

    List<WeekDTO> getWeeks();

    Optional<WeekDTO> getWeek(int weekId);

    List<WeekDTO> getWeeksByCampaignUUID(UUID uuid);

    List<WeekDTO> getWeeksByMonth(int monthId);

    void saveWeek(WeekDTO week);

    void deleteWeek(int weekId);

    Optional<WeekDTO> updateWeek(int weekId, WeekDTO week);
}
