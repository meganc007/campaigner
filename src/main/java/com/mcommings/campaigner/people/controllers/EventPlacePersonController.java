package com.mcommings.campaigner.people.controllers;

import com.mcommings.campaigner.people.entities.EventPlacePerson;
import com.mcommings.campaigner.people.services.EventPlacePersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/people/events-places-people")
public class EventPlacePersonController {

    private final EventPlacePersonService eventPlacePersonService;

    @Autowired
    public EventPlacePersonController(EventPlacePersonService eventPlacePersonService) {
        this.eventPlacePersonService = eventPlacePersonService;
    }

    @GetMapping
    public List<EventPlacePerson> getEventsPlacesPeople() {
        return eventPlacePersonService.getEventsPlacesPeople();
    }

    @GetMapping(path = "/campaign/{uuid}")
    public List<EventPlacePerson> getEventsPlacesPeopleByCampaignUUID(@PathVariable("uuid") UUID uuid) {
        return eventPlacePersonService.getEventsPlacesPeopleByCampaignUUID(uuid);
    }

    @GetMapping(path = "/person/{personId}")
    public List<EventPlacePerson> getEventsPlacesPeopleByPerson(@PathVariable("personId") int personId) {
        return eventPlacePersonService.getEventsPlacesPeopleByPerson(personId);
    }

    @GetMapping(path = "/place/{placeId}")
    public List<EventPlacePerson> getEventsPlacesPeopleByPlace(@PathVariable("placeId") int placeId) {
        return eventPlacePersonService.getEventsPlacesPeopleByPlace(placeId);
    }

    @GetMapping(path = "/event/{eventId}")
    public List<EventPlacePerson> getEventsPlacesPeopleByEvent(@PathVariable("eventId") int eventId) {
        return eventPlacePersonService.getEventsPlacesPeopleByEvent(eventId);
    }

    @PostMapping
    public void saveEventPlacePerson(@RequestBody EventPlacePerson eventPlacePerson) {
        eventPlacePersonService.saveEventPlacePerson(eventPlacePerson);
    }

    @DeleteMapping(path = "{id}")
    public void deleteEventPlacePerson(@PathVariable("id") int id) {
        eventPlacePersonService.deleteEventPlacePerson(id);
    }

    @PutMapping(path = "{id}")
    public void updateEventPlacePerson(@PathVariable("id") int id, @RequestBody EventPlacePerson eventPlacePerson) {
        eventPlacePersonService.updateEventPlacePerson(id, eventPlacePerson);
    }
}
