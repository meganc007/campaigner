package com.mcommings.campaigner.common.controllers;

import com.mcommings.campaigner.common.entities.Event;
import com.mcommings.campaigner.common.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/events")
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public List<Event> getEvents() {
        return eventService.getEvents();
    }

    @GetMapping(path = "/campaign/{uuid}")
    public List<Event> getEventsByCampaignUUID(@PathVariable("uuid") UUID uuid) {
        return eventService.getEventsByCampaignUUID(uuid);
    }

    @GetMapping(path = "/continent/{continentId}")
    public List<Event> getEventsByContinent(@PathVariable("continentId") int continentId) {
        return eventService.getEventsByContinent(continentId);
    }

    @GetMapping(path = "/country/{countryId}")
    public List<Event> getEventsByCountry(@PathVariable("countryId") int countryId) {
        return eventService.getEventsByCountry(countryId);
    }

    @GetMapping(path = "/city/{cityId}")
    public List<Event> getEventsByCity(@PathVariable("cityId") int cityId) {
        return eventService.getEventsByCity(cityId);
    }

    @PostMapping
    public void saveEvent(@RequestBody Event event) {
        eventService.saveEvent(event);
    }

    @DeleteMapping(path = "{eventId}")
    public void deleteEvent(@PathVariable("eventId") int eventId) {
        eventService.deleteEvent(eventId);
    }

    @PutMapping(path = "{eventId}")
    public void updateEvent(@PathVariable("eventId") int eventId, @RequestBody Event event) {
        eventService.updateEvent(eventId, event);
    }
}
