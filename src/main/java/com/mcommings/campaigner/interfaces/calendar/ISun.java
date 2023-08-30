package com.mcommings.campaigner.interfaces.calendar;

import com.mcommings.campaigner.models.calendar.Sun;

import java.util.List;

public interface ISun {
    List<Sun> getSuns();

    void saveSun(Sun sun);

    void deleteSun(int sunId);

    void updateSun(int sunId, Sun sun);
}
