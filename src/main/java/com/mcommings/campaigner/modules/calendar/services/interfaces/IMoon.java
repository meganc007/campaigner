package com.mcommings.campaigner.modules.calendar.services.interfaces;

import com.mcommings.campaigner.modules.calendar.dtos.MoonDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IMoon {

    List<MoonDTO> getMoons();

    List<MoonDTO> getMoonsByCampaignUUID(UUID uuid);

    void saveMoon(MoonDTO moon);

    void deleteMoon(int moonId);

    Optional<MoonDTO> updateMoon(int moonId, MoonDTO moon);
}
