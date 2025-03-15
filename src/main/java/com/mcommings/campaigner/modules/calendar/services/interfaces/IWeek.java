package com.mcommings.campaigner.modules.calendar.services.interfaces;

import com.mcommings.campaigner.modules.calendar.entities.Week;

import java.util.List;
import java.util.UUID;

public interface IWeek {

    List<Week> getWeeks();

    List<Week> getWeeksByCampaignUUID(UUID uuid);

    List<Week> getWeeksByMonth(int monthId);

    void saveWeek(Week week);

    void deleteWeek(int weekId);

    void updateWeek(int weekId, Week week);
}
