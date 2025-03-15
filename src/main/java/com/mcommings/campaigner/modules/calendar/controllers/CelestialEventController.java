package com.mcommings.campaigner.modules.calendar.controllers;

import com.mcommings.campaigner.modules.calendar.entities.CelestialEvent;
import com.mcommings.campaigner.modules.calendar.services.CelestialEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/calendar/celestial-events")
public class CelestialEventController {
    
    private final CelestialEventService celestialEventService;

    @Autowired
    public CelestialEventController(CelestialEventService celestialEventService) {
        this.celestialEventService = celestialEventService;
    }

    @GetMapping
    public List<CelestialEvent> getCelestialEvents() {
        return celestialEventService.getCelestialEvents();
    }

    @GetMapping(path = "/campaign/{uuid}")
    public List<CelestialEvent> getCelestialEventsByCampaignUUID(@PathVariable("uuid") UUID uuid) {
        return celestialEventService.getCelestialEventsByCampaignUUID(uuid);
    }

    @GetMapping(path = "/moon/{moonId}")
    public List<CelestialEvent> getCelestialEventsByMoon(@PathVariable("moonId") int moonId) {
        return celestialEventService.getCelestialEventsByMoon(moonId);
    }

    @GetMapping(path = "/sun/{sunId}")
    public List<CelestialEvent> getCelestialEventsBySun(@PathVariable("sunId") int sunId) {
        return celestialEventService.getCelestialEventsBySun(sunId);
    }

    @GetMapping(path = "/month/{monthId}")
    public List<CelestialEvent> getCelestialEventsByMonth(@PathVariable("monthId") int monthId) {
        return celestialEventService.getCelestialEventsByMonth(monthId);
    }

    @PostMapping
    public void saveCelestialEvent(@RequestBody CelestialEvent celestialEvent) {
        celestialEventService.saveCelestialEvent(celestialEvent);
    }

    @DeleteMapping(path = "{celestialEventId}")
    public void deleteCelestialEvent(@PathVariable("celestialEventId") int celestialEventId) {
        celestialEventService.deleteCelestialEvent(celestialEventId);
    }

    @PutMapping(path = "{celestialEventId}")
    public void updateCelestialEvent(@PathVariable("celestialEventId") int celestialEventId, @RequestBody CelestialEvent celestialEvent) {
        celestialEventService.updateCelestialEvent(celestialEventId, celestialEvent);
    }
}
