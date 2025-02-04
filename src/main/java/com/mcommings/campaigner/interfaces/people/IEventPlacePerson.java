package com.mcommings.campaigner.interfaces.people;

import com.mcommings.campaigner.entities.people.EventPlacePerson;

import java.util.List;
import java.util.UUID;

public interface IEventPlacePerson {

    List<EventPlacePerson> getEventsPlacesPeople();

    List<EventPlacePerson> getEventsPlacesPeopleByCampaignUUID(UUID uuid);

    List<EventPlacePerson> getEventsPlacesPeopleByPerson(int personId);

    List<EventPlacePerson> getEventsPlacesPeopleByPlace(int placeId);

    List<EventPlacePerson> getEventsPlacesPeopleByEvent(int eventId);

    void saveEventPlacePerson(EventPlacePerson eventPlacePerson);

    void deleteEventPlacePerson(int id);

    void updateEventPlacePerson(int id, EventPlacePerson eventPlacePerson);
}
