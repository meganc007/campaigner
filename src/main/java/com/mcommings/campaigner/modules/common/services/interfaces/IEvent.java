package com.mcommings.campaigner.modules.common.services.interfaces;

import com.mcommings.campaigner.modules.calendar.dtos.EventDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IEvent {

    List<EventDTO> getEvents();

    Optional<EventDTO> getEvent(int eventId);

    List<EventDTO> getEventsByCampaignUUID(UUID uuid);

    List<EventDTO> getEventsByYear(int year);

    List<EventDTO> getEventsByMonth(int monthId);

    List<EventDTO> getEventsByWeek(int weekId);

    List<EventDTO> getEventsByDay(int dayId);

    List<EventDTO> getEventsByContinent(int continentId);

    List<EventDTO> getEventsByCountry(int countryId);

    List<EventDTO> getEventsByCity(int cityId);

    void saveEvent(EventDTO event);

    void deleteEvent(int eventId);

    Optional<EventDTO> updateEvent(int eventId, EventDTO event);
}
