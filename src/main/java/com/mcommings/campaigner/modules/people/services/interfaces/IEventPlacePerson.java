package com.mcommings.campaigner.modules.people.services.interfaces;

import com.mcommings.campaigner.modules.people.dtos.EventPlacePersonDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IEventPlacePerson {

    List<EventPlacePersonDTO> getEventsPlacesPeople();

    Optional<EventPlacePersonDTO> getEventPlacePerson(int eventPlacePersonId);

    List<EventPlacePersonDTO> getEventsPlacesPeopleByCampaignUUID(UUID uuid);

    List<EventPlacePersonDTO> getEventsPlacesPeopleByPerson(int personId);

    List<EventPlacePersonDTO> getEventsPlacesPeopleByPlace(int placeId);

    List<EventPlacePersonDTO> getEventsPlacesPeopleByEvent(int eventId);

    void saveEventPlacePerson(EventPlacePersonDTO eventPlacePerson);

    void deleteEventPlacePerson(int id);

    Optional<EventPlacePersonDTO> updateEventPlacePerson(int id, EventPlacePersonDTO eventPlacePerson);
}
