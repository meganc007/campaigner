package com.mcommings.campaigner.interfaces;

import com.mcommings.campaigner.models.Event;

import java.util.List;

public interface IEvent {

    List<Event> getEvents();

    void saveEvent(Event event);

    void deleteEvent(int eventId);

    void updateEvent(int eventId, Event event);
}
