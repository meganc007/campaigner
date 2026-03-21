package com.mcommings.campaigner.modules.calendar.controllers;

import com.mcommings.campaigner.modules.calendar.dtos.celestial_events.CreateCelestialEventDTO;
import com.mcommings.campaigner.modules.calendar.dtos.celestial_events.UpdateCelestialEventDTO;
import com.mcommings.campaigner.modules.calendar.dtos.celestial_events.ViewCelestialEventDTO;
import com.mcommings.campaigner.modules.calendar.services.CelestialEventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/celestialevents")
public class CelestialEventController {

    private final CelestialEventService celestialEventService;

    @GetMapping
    public List<ViewCelestialEventDTO> getCelestialEvents() {

        return celestialEventService.getAll();
    }

    @GetMapping(path = "/{celestialEventId}")
    public ViewCelestialEventDTO getCelestialEvent(@PathVariable int celestialEventId) {
        return celestialEventService.getById(celestialEventId);
    }

    @GetMapping(path = "/campaign/{uuid}")
    public List<ViewCelestialEventDTO> getCelestialEventsByCampaignUUID(@PathVariable UUID uuid) {
        return celestialEventService.getCelestialEventsByCampaignUUID(uuid);
    }

    @GetMapping(path = "/moon/{moonId}")
    public List<ViewCelestialEventDTO> getCelestialEventsByMoonId(
            @PathVariable int moonId) {

        return celestialEventService.getCelestialEventsByMoonId(moonId);
    }

    @GetMapping(path = "/sun/{sunId}")
    public List<ViewCelestialEventDTO> getCelestialEventsBySunId(
            @PathVariable int sunId) {

        return celestialEventService.getCelestialEventsBySunId(sunId);
    }

    @GetMapping(path = "/month/{monthId}")
    public List<ViewCelestialEventDTO> getCelestialEventsByMonthId(
            @PathVariable int monthId) {

        return celestialEventService.getCelestialEventsByMonthId(monthId);
    }

    @GetMapping(path = "/year/{yearId}")
    public List<ViewCelestialEventDTO> getCelestialEventsByYearId(
            @PathVariable int yearId) {

        return celestialEventService.getCelestialEventsByYearId(yearId);
    }

    @PostMapping
    public ViewCelestialEventDTO createCelestialEvent(@Valid @RequestBody CreateCelestialEventDTO celestialEvent) {
        return celestialEventService.create(celestialEvent);
    }

    @PutMapping
    public ViewCelestialEventDTO updateCelestialEvent(@Valid @RequestBody UpdateCelestialEventDTO celestialEvent) {
        return celestialEventService.update(celestialEvent);
    }

    @DeleteMapping(path = "/{celestialEventId}")
    public void deleteCelestialEvent(@PathVariable int celestialEventId) {

        celestialEventService.delete(celestialEventId);
    }
}
