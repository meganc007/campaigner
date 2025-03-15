package com.mcommings.campaigner.modules.calendar.controllers;

import com.mcommings.campaigner.modules.calendar.dtos.CelestialEventDTO;
import com.mcommings.campaigner.modules.calendar.services.interfaces.ICelestialEvent;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.mcommings.campaigner.enums.ErrorMessage.ID_NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/celestialevents")
public class CelestialEventController {

    private final ICelestialEvent celestialEventService;

    @GetMapping
    public List<CelestialEventDTO> getCelestialEvents() {
        return celestialEventService.getCelestialEvents();
    }

    @GetMapping(path = "/{celestialEventId}")
    public CelestialEventDTO getCelestialEvent(@PathVariable("celestialEventId") int celestialEventId) {
        return celestialEventService.getCelestialEvent(celestialEventId).orElseThrow(() -> new IllegalArgumentException(ID_NOT_FOUND.message));
    }

    @GetMapping(path = "/campaign/{uuid}")
    public List<CelestialEventDTO> getCelestialEventsByCampaignUUID(@PathVariable("uuid") UUID uuid) {
        return celestialEventService.getCelestialEventsByCampaignUUID(uuid);
    }

    @GetMapping(path = "/moon/{moonId}")
    public List<CelestialEventDTO> getCelestialEventsByMoon(@PathVariable("moonId") int moonId) {
        return celestialEventService.getCelestialEventsByMoon(moonId);
    }

    @GetMapping(path = "/sun/{sunId}")
    public List<CelestialEventDTO> getCelestialEventsBySun(@PathVariable("sunId") int sunId) {
        return celestialEventService.getCelestialEventsBySun(sunId);
    }

    @GetMapping(path = "/month/{monthId}")
    public List<CelestialEventDTO> getCelestialEventsByMonth(@PathVariable("monthId") int monthId) {
        return celestialEventService.getCelestialEventsByMonth(monthId);
    }

    @PostMapping
    public void saveCelestialEvent(@Valid @RequestBody CelestialEventDTO celestialEvent) {
        celestialEventService.saveCelestialEvent(celestialEvent);
    }

    @DeleteMapping(path = "{celestialEventId}")
    public void deleteCelestialEvent(@PathVariable("celestialEventId") int celestialEventId) {
        celestialEventService.deleteCelestialEvent(celestialEventId);
    }

    @PutMapping(path = "{celestialEventId}")
    public void updateCelestialEvent(@PathVariable("celestialEventId") int celestialEventId, @RequestBody CelestialEventDTO celestialEvent) {
        celestialEventService.updateCelestialEvent(celestialEventId, celestialEvent);
    }
}
