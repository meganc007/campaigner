package com.mcommings.campaigner.interfaces;

import com.mcommings.campaigner.models.Week;

import java.util.List;

public interface IWeek {

    List<Week> getWeeks();

    void saveWeek(Week week);

    void deleteWeek(int weekId);

    void updateWeek(int weekId, Week week);
}
