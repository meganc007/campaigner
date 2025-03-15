package com.mcommings.campaigner.calendar.services.interfaces;

import com.mcommings.campaigner.calendar.entities.Moon;

import java.util.List;
import java.util.UUID;

public interface IMoon {

    List<Moon> getMoons();

    List<Moon> getMoonsByCampaignUUID(UUID uuid);

    void saveMoon(Moon moon);

    void deleteMoon(int moonId);

    void updateMoon(int moonId, Moon moon);
}
