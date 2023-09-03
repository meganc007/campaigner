package com.mcommings.campaigner.interfaces.people;

import com.mcommings.campaigner.models.people.EventPlacePerson;

import java.util.List;

public interface IEventPlacePerson {

    List<EventPlacePerson> getEventsPlacesPeople();

    void saveEventPlacePerson(EventPlacePerson eventPlacePerson);

    void deleteEventPlacePerson(int id);

    void updateEventPlacePerson(int id, EventPlacePerson eventPlacePerson);
}
