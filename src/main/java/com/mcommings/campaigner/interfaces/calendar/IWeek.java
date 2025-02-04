package com.mcommings.campaigner.interfaces.calendar;

import com.mcommings.campaigner.entities.calendar.Week;

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
