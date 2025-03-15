package com.mcommings.campaigner.calendar.services.interfaces;

import com.mcommings.campaigner.calendar.entities.CelestialEvent;

import java.util.List;
import java.util.UUID;

public interface ICelestialEvent {

    List<CelestialEvent> getCelestialEvents();

    List<CelestialEvent> getCelestialEventsByCampaignUUID(UUID uuid);

    List<CelestialEvent> getCelestialEventsByMoon(int moonId);

    List<CelestialEvent> getCelestialEventsBySun(int sunId);

    List<CelestialEvent> getCelestialEventsByMonth(int monthId);

    void saveCelestialEvent(CelestialEvent celestialEvent);

    void deleteCelestialEvent(int celestialEventId);

    void updateCelestialEvent(int celestialEventId, CelestialEvent celestialEvent);
}
