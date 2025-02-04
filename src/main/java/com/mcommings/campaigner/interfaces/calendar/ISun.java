package com.mcommings.campaigner.interfaces.calendar;

import com.mcommings.campaigner.entities.calendar.Sun;

import java.util.List;
import java.util.UUID;

public interface ISun {
    List<Sun> getSuns();

    List<Sun> getSunsByCampaignUUID(UUID uuid);

    void saveSun(Sun sun);

    void deleteSun(int sunId);

    void updateSun(int sunId, Sun sun);
}
