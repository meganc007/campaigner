package com.mcommings.campaigner.interfaces.calendar;

import com.mcommings.campaigner.models.calendar.CelestialEvent;

import java.util.List;

public interface ICelestialEvent {
    
    List<CelestialEvent> getCelestialEvents();

    void saveCelestialEvent(CelestialEvent celestialEvent);

    void deleteCelestialEvent(int celestialEventId);

    void updateCelestialEvent(int celestialEventId, CelestialEvent celestialEvent);
}
