package com.mcommings.campaigner.modules.common.controllers;

import com.mcommings.campaigner.modules.common.dtos.EventDTO;
import com.mcommings.campaigner.modules.common.services.interfaces.IEvent;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.mcommings.campaigner.enums.ErrorMessage.ID_NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/events")
public class EventController {

    private final IEvent eventService;
    
    @GetMapping
    public List<EventDTO> getEvents() {
        return eventService.getEvents();
    }

    @GetMapping(path = "/{eventId}")
    public EventDTO getEvent(@PathVariable("eventId") int eventId) {
        return eventService.getEvent(eventId).orElseThrow(() -> new IllegalArgumentException(ID_NOT_FOUND.message));
    }

    @GetMapping(path = "/campaign/{uuid}")
    public List<EventDTO> getEventsByCampaignUUID(@PathVariable("uuid") UUID uuid) {
        return eventService.getEventsByCampaignUUID(uuid);
    }

    @GetMapping(path = "/year/{year}")
    public List<EventDTO> getEventsByYear(@PathVariable("year") int year) {
        return eventService.getEventsByYear(year);
    }

    @GetMapping(path = "/month/{monthId}")
    public List<EventDTO> getEventsByMonth(@PathVariable("monthId") int monthId) {
        return eventService.getEventsByMonth(monthId);
    }

    @GetMapping(path = "/week/{weekId}")
    public List<EventDTO> getEventsByWeek(@PathVariable("weekId") int weekId) {
        return eventService.getEventsByWeek(weekId);
    }

    @GetMapping(path = "/day/{dayId}")
    public List<EventDTO> getEventsByDay(@PathVariable("dayId") int dayId) {
        return eventService.getEventsByDay(dayId);
    }

    @GetMapping(path = "/continent/{continentId}")
    public List<EventDTO> getEventsByContinent(@PathVariable("continentId") int continentId) {
        return eventService.getEventsByContinent(continentId);
    }

    @GetMapping(path = "/country/{countryId}")
    public List<EventDTO> getEventsByCountry(@PathVariable("countryId") int countryId) {
        return eventService.getEventsByCountry(countryId);
    }

    @GetMapping(path = "/city/{cityId}")
    public List<EventDTO> getEventsByCity(@PathVariable("cityId") int cityId) {
        return eventService.getEventsByCity(cityId);
    }

    @PostMapping
    public void saveEvent(@Valid @RequestBody EventDTO event) {
        eventService.saveEvent(event);
    }

    @DeleteMapping(path = "{eventId}")
    public void deleteEvent(@PathVariable("eventId") int eventId) {
        eventService.deleteEvent(eventId);
    }

    @PutMapping(path = "{eventId}")
    public void updateEvent(@PathVariable("eventId") int eventId, @RequestBody EventDTO event) {
        eventService.updateEvent(eventId, event);
    }
}
