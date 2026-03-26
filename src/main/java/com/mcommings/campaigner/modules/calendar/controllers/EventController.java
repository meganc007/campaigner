package com.mcommings.campaigner.modules.calendar.controllers;

import com.mcommings.campaigner.modules.calendar.dtos.events.CreateEventDTO;
import com.mcommings.campaigner.modules.calendar.dtos.events.UpdateEventDTO;
import com.mcommings.campaigner.modules.calendar.dtos.events.ViewEventDTO;
import com.mcommings.campaigner.modules.calendar.services.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/events")
public class EventController {

    private final EventService eventService;

    @GetMapping
    public List<ViewEventDTO> getEvents() {

        return eventService.getAll();
    }

    @GetMapping(path = "/{eventId}")
    public ViewEventDTO getEvent(@PathVariable int eventId) {
        return eventService.getById(eventId);
    }

    @GetMapping(path = "/campaign/{uuid}")
    public List<ViewEventDTO> getEventsByCampaignUUID(@PathVariable UUID uuid) {
        return eventService.getEventsByCampaignUUID(uuid);
    }

    @GetMapping(path = "/year/{year}")
    public List<ViewEventDTO> getEventsByYear(
            @PathVariable int year) {

        return eventService.getEventsByYear(year);
    }

    @GetMapping(path = "/month/{monthId}")
    public List<ViewEventDTO> getEventsByMonthId(
            @PathVariable int monthId) {

        return eventService.getEventsByMonthId(monthId);
    }

    @GetMapping(path = "/week/{weekId}")
    public List<ViewEventDTO> getEventsByWeekId(
            @PathVariable int weekId) {

        return eventService.getEventsByWeekId(weekId);
    }

    @GetMapping(path = "/day/{dayId}")
    public List<ViewEventDTO> getEventsByDayId(
            @PathVariable int dayId) {

        return eventService.getEventsByDayId(dayId);
    }

    @GetMapping(path = "/continent/{continentId}")
    public List<ViewEventDTO> getEventsByContinentId(
            @PathVariable int continentId) {

        return eventService.getEventsByContinentId(continentId);
    }

    @GetMapping(path = "/country/{countryId}")
    public List<ViewEventDTO> getEventsByCountryId(
            @PathVariable int countryId) {

        return eventService.getEventsByCountryId(countryId);
    }

    @GetMapping(path = "/city/{cityId}")
    public List<ViewEventDTO> getEventsByCityId(
            @PathVariable int cityId) {

        return eventService.getEventsByCityId(cityId);
    }

    @PostMapping
    public ViewEventDTO createEvent(@Valid @RequestBody CreateEventDTO event) {
        return eventService.create(event);
    }

    @PutMapping
    public ViewEventDTO updateEvent(@Valid @RequestBody UpdateEventDTO event) {
        return eventService.update(event);
    }

    @DeleteMapping(path = "/{eventId}")
    public void deleteEvent(@PathVariable int eventId) {

        eventService.delete(eventId);
    }
}
