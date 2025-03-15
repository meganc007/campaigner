package com.mcommings.campaigner.modules.calendar.services.interfaces;

import com.mcommings.campaigner.modules.calendar.dtos.SunDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ISun {
    List<SunDTO> getSuns();

    List<SunDTO> getSunsByCampaignUUID(UUID uuid);

    void saveSun(SunDTO sun);

    void deleteSun(int sunId);

    Optional<SunDTO> updateSun(int sunId, SunDTO sun);
}
