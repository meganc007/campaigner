package com.mcommings.campaigner.modules.people.controllers;

import com.mcommings.campaigner.modules.people.dtos.EventPlacePersonDTO;
import com.mcommings.campaigner.modules.people.services.interfaces.IEventPlacePerson;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.mcommings.campaigner.enums.ErrorMessage.ID_NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/eventsPlacesPeople")
public class EventPlacePersonController {

    private final IEventPlacePerson eventPlacePersonService;

    @GetMapping
    public List<EventPlacePersonDTO> getEventsPlacesPeople() {
        return eventPlacePersonService.getEventsPlacesPeople();
    }

    @GetMapping(path = "/{eventPlacePersonId}")
    public EventPlacePersonDTO getEventPlacePerson(@PathVariable("eventPlacePersonId") int eventPlacePersonId) {
        return eventPlacePersonService.getEventPlacePerson(eventPlacePersonId).orElseThrow(() -> new IllegalArgumentException(ID_NOT_FOUND.message));
    }

    @GetMapping(path = "/campaign/{uuid}")
    public List<EventPlacePersonDTO> getEventsPlacesPeopleByCampaignUUID(@PathVariable("uuid") UUID uuid) {
        return eventPlacePersonService.getEventsPlacesPeopleByCampaignUUID(uuid);
    }

    @GetMapping(path = "/person/{personId}")
    public List<EventPlacePersonDTO> getEventsPlacesPeopleByPerson(@PathVariable("personId") int personId) {
        return eventPlacePersonService.getEventsPlacesPeopleByPerson(personId);
    }

    @GetMapping(path = "/place/{placeId}")
    public List<EventPlacePersonDTO> getEventsPlacesPeopleByPlace(@PathVariable("placeId") int placeId) {
        return eventPlacePersonService.getEventsPlacesPeopleByPlace(placeId);
    }

    @GetMapping(path = "/event/{eventId}")
    public List<EventPlacePersonDTO> getEventsPlacesPeopleByEvent(@PathVariable("eventId") int eventId) {
        return eventPlacePersonService.getEventsPlacesPeopleByEvent(eventId);
    }

    @PostMapping
    public void saveEventPlacePerson(@Valid @RequestBody EventPlacePersonDTO eventPlacePerson) {
        eventPlacePersonService.saveEventPlacePerson(eventPlacePerson);
    }

    @DeleteMapping(path = "{id}")
    public void deleteEventPlacePerson(@PathVariable("id") int id) {
        eventPlacePersonService.deleteEventPlacePerson(id);
    }

    @PutMapping(path = "{id}")
    public void updateEventPlacePerson(@PathVariable("id") int id, @RequestBody EventPlacePersonDTO eventPlacePerson) {
        eventPlacePersonService.updateEventPlacePerson(id, eventPlacePerson);
    }
}
