package com.mcommings.campaigner.controllers.people;

import com.mcommings.campaigner.models.people.EventPlacePerson;
import com.mcommings.campaigner.services.people.EventPlacePersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/people/events-places-people")
public class EventPlacePersonController {

    private final EventPlacePersonService eventPlacePersonService;

    @Autowired
    public EventPlacePersonController(EventPlacePersonService eventPlacePersonService) {
        this.eventPlacePersonService = eventPlacePersonService;
    }

    @GetMapping
    List<EventPlacePerson> getEventsPlacesPeople() {
        return eventPlacePersonService.getEventsPlacesPeople();
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
