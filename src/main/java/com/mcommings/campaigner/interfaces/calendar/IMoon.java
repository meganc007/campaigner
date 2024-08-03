package com.mcommings.campaigner.interfaces.calendar;

import com.mcommings.campaigner.models.calendar.Moon;

import java.util.List;
import java.util.UUID;

public interface IMoon {

    List<Moon> getMoons();

    List<Moon> getMoonsByCampaignUUID(UUID uuid);

    void saveMoon(Moon moon);

    void deleteMoon(int moonId);

    void updateMoon(int moonId, Moon moon);
}
