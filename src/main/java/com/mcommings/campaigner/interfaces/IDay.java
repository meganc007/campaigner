package com.mcommings.campaigner.interfaces;

import com.mcommings.campaigner.models.Day;

import java.util.List;

public interface IDay {

    List<Day> getDays();

    void saveDay(Day day);

    void deleteDay(int dayId);

    void updateDay(int dayId, Day day);
}
