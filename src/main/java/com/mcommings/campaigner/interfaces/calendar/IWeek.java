package com.mcommings.campaigner.interfaces.calendar;

import com.mcommings.campaigner.models.calendar.Week;

import java.util.List;

public interface IWeek {

    List<Week> getWeeks();

    void saveWeek(Week week);

    void deleteWeek(int weekId);

    void updateWeek(int weekId, Week week);
}
