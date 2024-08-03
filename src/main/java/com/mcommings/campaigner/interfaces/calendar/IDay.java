package com.mcommings.campaigner.interfaces.calendar;

import com.mcommings.campaigner.models.calendar.Day;

import java.util.List;
import java.util.UUID;

public interface IDay {

    List<Day> getDays();

    List<Day> getDaysByCampaignUUID(UUID uuid);

    List<Day> getDaysByWeek(int weekId);

    void saveDay(Day day);

    void deleteDay(int dayId);

    void updateDay(int dayId, Day day);
}
