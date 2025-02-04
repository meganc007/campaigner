package com.mcommings.campaigner.interfaces;

import com.mcommings.campaigner.entities.Event;

import java.util.List;
import java.util.UUID;

public interface IEvent {

    List<Event> getEvents();

    List<Event> getEventsByCampaignUUID(UUID uuid);

    List<Event> getEventsByContinent(int continentId);

    List<Event> getEventsByCountry(int countryId);

    List<Event> getEventsByCity(int cityId);

    void saveEvent(Event event);

    void deleteEvent(int eventId);

    void updateEvent(int eventId, Event event);
}
