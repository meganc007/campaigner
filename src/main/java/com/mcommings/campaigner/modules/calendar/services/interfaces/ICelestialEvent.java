package com.mcommings.campaigner.modules.calendar.services.interfaces;

import com.mcommings.campaigner.modules.calendar.dtos.CelestialEventDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ICelestialEvent {

    List<CelestialEventDTO> getCelestialEvents();

    List<CelestialEventDTO> getCelestialEventsByCampaignUUID(UUID uuid);

    List<CelestialEventDTO> getCelestialEventsByMoon(int moonId);

    List<CelestialEventDTO> getCelestialEventsBySun(int sunId);

    List<CelestialEventDTO> getCelestialEventsByMonth(int monthId);

    void saveCelestialEvent(CelestialEventDTO celestialEvent);

    void deleteCelestialEvent(int celestialEventId);

    Optional<CelestialEventDTO> updateCelestialEvent(int celestialEventId, CelestialEventDTO celestialEvent);
}
