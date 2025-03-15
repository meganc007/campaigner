package com.mcommings.campaigner.calendar.services.interfaces;

import com.mcommings.campaigner.calendar.entities.Day;

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
