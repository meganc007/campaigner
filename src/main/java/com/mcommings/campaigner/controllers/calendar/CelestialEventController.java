package com.mcommings.campaigner.controllers.calendar;

import com.mcommings.campaigner.models.calendar.CelestialEvent;
import com.mcommings.campaigner.services.calendar.CelestialEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
