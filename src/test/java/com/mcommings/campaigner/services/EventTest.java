package com.mcommings.campaigner.services;

import com.mcommings.campaigner.calendar.repositories.IDayRepository;
import com.mcommings.campaigner.calendar.repositories.IMonthRepository;
import com.mcommings.campaigner.calendar.repositories.IWeekRepository;
import com.mcommings.campaigner.common.entities.Event;
import com.mcommings.campaigner.common.entities.RepositoryHelper;
import com.mcommings.campaigner.common.repositories.IEventRepository;
import com.mcommings.campaigner.common.services.EventService;
import com.mcommings.campaigner.locations.repositories.ICityRepository;
import com.mcommings.campaigner.locations.repositories.IContinentRepository;
import com.mcommings.campaigner.locations.repositories.ICountryRepository;
import com.mcommings.campaigner.people.entities.EventPlacePerson;
import com.mcommings.campaigner.people.repositories.IEventPlacePersonRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;

import java.util.*;

import static com.mcommings.campaigner.enums.ForeignKey.FK_EVENT;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class EventTest {

    @Mock
    private IEventRepository eventRepository;
    @Mock
    private IMonthRepository monthRepository;
    @Mock
    private IWeekRepository weekRepository;
    @Mock
    private IDayRepository dayRepository;
    @Mock
    private ICityRepository cityRepository;
    @Mock
    private IContinentRepository continentRepository;
    @Mock
    private ICountryRepository countryRepository;
    @Mock
    private IEventPlacePersonRepository eventPlacePersonRepository;

    @InjectMocks
    private EventService eventService;

    @Test
    public void whenThereAreEvents_getEvents_ReturnsEvents() {
        List<Event> events = new ArrayList<>();
        UUID campaign = UUID.randomUUID();
        events.add(new Event(1, "Event 1", "Description 1", 1, campaign));
        events.add(new Event(2, "Event 2", "Description 2", 1, campaign));
        events.add(new Event(3, "Event 3", "Description 3", 1, 2, 3, 4, 5, 6, 7, campaign));
        when(eventRepository.findAll()).thenReturn(events);

        List<Event> result = eventService.getEvents();

        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals(events, result);
    }

    @Test
    public void whenThereAreNoEvents_getEvents_ReturnsNothing() {
        List<Event> events = new ArrayList<>();
        when(eventRepository.findAll()).thenReturn(events);

        List<Event> result = eventService.getEvents();

        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(events, result);
    }

    @Test
    public void whenCampaignUUIDIsValid_getEventsByCampaignUUID_ReturnsEvents() {
        List<Event> events = new ArrayList<>();
        UUID campaign = UUID.randomUUID();
        events.add(new Event(1, "Event 1", "Description 1", 1, campaign));
        events.add(new Event(2, "Event 2", "Description 2", 1, campaign));
        events.add(new Event(3, "Event 3", "Description 3", 1, 2, 3, 4, 5, 6, 7, campaign));
        when(eventRepository.findByfk_campaign_uuid(campaign)).thenReturn(events);

        List<Event> results = eventService.getEventsByCampaignUUID(campaign);

        Assertions.assertEquals(3, results.size());
        Assertions.assertEquals(events, results);
    }

    @Test
    public void whenCampaignUUIDIsInvalid_getEventsByCampaignUUID_ReturnsNothing() {
        UUID campaign = UUID.randomUUID();
        List<Event> events = new ArrayList<>();
        when(eventRepository.findByfk_campaign_uuid(campaign)).thenReturn(events);

        List<Event> result = eventService.getEventsByCampaignUUID(campaign);

        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(events, result);
    }

    @Test
    public void whenEventWithNoForeignKeysIsValid_saveEvent_SavesTheEvent() {
        Event event = new Event(1, "Event 1", "Description 1", 1, UUID.randomUUID());
        when(eventRepository.saveAndFlush(event)).thenReturn(event);

        assertDoesNotThrow(() -> eventService.saveEvent(event));

        verify(eventRepository, times(1)).saveAndFlush(event);
    }

    @Test
    public void whenEventWithForeignKeysIsValid_saveEvent_SavesTheEvent() {
        Event event = new Event(1, "Event 1", "Description 1", 1, 2, 3,
                4, 5, 6, 7, UUID.randomUUID());

        when(monthRepository.existsById(2)).thenReturn(true);
        when(weekRepository.existsById(3)).thenReturn(true);
        when(dayRepository.existsById(4)).thenReturn(true);
        when(cityRepository.existsById(5)).thenReturn(true);
        when(continentRepository.existsById(6)).thenReturn(true);
        when(countryRepository.existsById(7)).thenReturn(true);
        when(eventRepository.saveAndFlush(event)).thenReturn(event);

        assertDoesNotThrow(() -> eventService.saveEvent(event));

        verify(eventRepository, times(1)).saveAndFlush(event);
    }

    @Test
    public void whenEventNameIsInvalid_saveEvent_ThrowsIllegalArgumentException() {
        Event eventWithEmptyName = new Event(1, "", "Description 1", 1, UUID.randomUUID());
        Event eventWithNullName = new Event(2, null, "Description 2", 2, UUID.randomUUID());

        assertThrows(IllegalArgumentException.class, () -> eventService.saveEvent(eventWithEmptyName));
        assertThrows(IllegalArgumentException.class, () -> eventService.saveEvent(eventWithNullName));
    }

    @Test
    public void whenEventNameAlreadyExists_saveEvent_ThrowsDataIntegrityViolationException() {
        Event event = new Event(1, "Event 1", "Description 1", 1, 2, 3,
                4, 5, 6, 7, UUID.randomUUID());
        Event eventWithDuplicatedName = new Event(2, "Event 1", "Description 2", 8,
                9, 10, 11, 12, 13, 14, UUID.randomUUID());

        when(eventRepository.existsById(1)).thenReturn(true);
        when(monthRepository.existsById(2)).thenReturn(true);
        when(weekRepository.existsById(3)).thenReturn(true);
        when(dayRepository.existsById(4)).thenReturn(true);
        when(cityRepository.existsById(5)).thenReturn(true);
        when(continentRepository.existsById(6)).thenReturn(true);
        when(countryRepository.existsById(7)).thenReturn(true);

        when(eventRepository.existsById(2)).thenReturn(true);
        when(monthRepository.existsById(9)).thenReturn(true);
        when(weekRepository.existsById(10)).thenReturn(true);
        when(dayRepository.existsById(11)).thenReturn(true);
        when(cityRepository.existsById(12)).thenReturn(true);
        when(continentRepository.existsById(13)).thenReturn(true);
        when(countryRepository.existsById(14)).thenReturn(true);

        when(eventRepository.saveAndFlush(event)).thenReturn(event);
        when(eventRepository.saveAndFlush(eventWithDuplicatedName)).thenThrow(DataIntegrityViolationException.class);

        assertDoesNotThrow(() -> eventService.saveEvent(event));
        assertThrows(DataIntegrityViolationException.class, () -> eventService.saveEvent(eventWithDuplicatedName));
    }

    @Test
    public void whenEventHasInvalidForeignKeys_saveEvent_ThrowsDataIntegrityViolationException() {
        Event event = new Event(1, "Event 1", "Description 1", 1, 2, 3,
                4, 5, 6, 7, UUID.randomUUID());
        when(monthRepository.existsById(2)).thenReturn(true);
        when(weekRepository.existsById(3)).thenReturn(false);
        when(dayRepository.existsById(4)).thenReturn(true);
        when(cityRepository.existsById(5)).thenReturn(false);
        when(eventRepository.existsById(6)).thenReturn(true);
        when(countryRepository.existsById(7)).thenReturn(false);
        when(eventRepository.saveAndFlush(event)).thenReturn(event);

        assertThrows(DataIntegrityViolationException.class, () -> eventService.saveEvent(event));
    }

    @Test
    public void whenEventIdExists_deleteEvent_DeletesTheEvent() {
        int eventId = 1;
        when(eventRepository.existsById(eventId)).thenReturn(true);
        assertDoesNotThrow(() -> eventService.deleteEvent(eventId));
        verify(eventRepository, times(1)).deleteById(eventId);
    }

    @Test
    public void whenEventIdDoesNotExist_deleteEvent_ThrowsIllegalArgumentException() {
        int eventId = 9000;
        when(eventRepository.existsById(eventId)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> eventService.deleteEvent(eventId));
    }

    @Test
    public void whenEventIdIsAForeignKey_deleteEvent_ThrowsDataIntegrityViolationException() {
        int eventId = 1;
        EventPlacePerson epp = new EventPlacePerson(1, eventId, 1, 1, UUID.randomUUID());
        List<CrudRepository> repositories = new ArrayList<>(Arrays.asList(eventPlacePersonRepository));
        List<EventPlacePerson> epps = new ArrayList<>(Arrays.asList(epp));

        when(eventRepository.existsById(eventId)).thenReturn(true);
        when(eventPlacePersonRepository.findByfk_event(eventId)).thenReturn(epps);

        boolean actual = RepositoryHelper.isForeignKey(repositories, FK_EVENT.columnName, eventId);
        Assertions.assertTrue(actual);
        assertThrows(DataIntegrityViolationException.class, () -> eventService.deleteEvent(eventId));
    }

    @Test
    public void whenEventIdWithNoFKIsFound_updateEvent_UpdatesTheEvent() {
        int eventId = 1;
        UUID campaign = UUID.randomUUID();

        Event event = new Event(eventId, "Old Event Name", "Old Description", 1, campaign);
        Event eventToUpdate = new Event(eventId, "Updated Event Name", "Updated Description", 2, campaign);

        when(eventRepository.existsById(eventId)).thenReturn(true);
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        eventService.updateEvent(eventId, eventToUpdate);

        verify(eventRepository).findById(eventId);

        Event result = eventRepository.findById(eventId).get();
        Assertions.assertEquals(eventToUpdate.getName(), result.getName());
        Assertions.assertEquals(eventToUpdate.getDescription(), result.getDescription());
        Assertions.assertEquals(eventToUpdate.getEvent_year(), result.getEvent_year());
    }

    @Test
    public void whenEventIdWithValidFKIsFound_updateEvent_UpdatesTheEvent() {
        int eventId = 2;
        UUID campaign = UUID.randomUUID();

        Event event = new Event(eventId, "Test Event Name", "Test Description", 1, campaign);
        Event eventToUpdate = new Event(eventId, "Updated Event Name", "Updated Description",
                1, 2, 3, 4, 5, 6, 7, campaign);

        when(eventRepository.existsById(eventId)).thenReturn(true);
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(monthRepository.existsById(2)).thenReturn(true);
        when(weekRepository.existsById(3)).thenReturn(true);
        when(dayRepository.existsById(4)).thenReturn(true);
        when(cityRepository.existsById(5)).thenReturn(true);
        when(continentRepository.existsById(6)).thenReturn(true);
        when(countryRepository.existsById(7)).thenReturn(true);

        eventService.updateEvent(eventId, eventToUpdate);

        verify(eventRepository).findById(eventId);

        Event result = eventRepository.findById(eventId).get();
        Assertions.assertEquals(eventToUpdate.getName(), result.getName());
        Assertions.assertEquals(eventToUpdate.getDescription(), result.getDescription());
        Assertions.assertEquals(eventToUpdate.getEvent_year(), result.getEvent_year());
        Assertions.assertEquals(eventToUpdate.getFk_month(), result.getFk_month());
        Assertions.assertEquals(eventToUpdate.getFk_week(), result.getFk_week());
        Assertions.assertEquals(eventToUpdate.getFk_day(), result.getFk_day());
        Assertions.assertEquals(eventToUpdate.getFk_city(), result.getFk_city());
        Assertions.assertEquals(eventToUpdate.getFk_continent(), result.getFk_continent());
        Assertions.assertEquals(eventToUpdate.getFk_country(), result.getFk_country());
    }

    @Test
    public void whenEventIdWithInvalidFKIsFound_updateEvent_ThrowsDataIntegrityViolationException() {
        int eventId = 2;
        UUID campaign = UUID.randomUUID();

        Event event = new Event(eventId, "Test Event Name", "Test Description", 1, campaign);
        Event eventToUpdate = new Event(eventId, "Updated Event Name", "Updated Description",
                1, 2, 3, 4, 5, 6, 7, campaign);

        when(eventRepository.existsById(eventId)).thenReturn(true);
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        when(monthRepository.existsById(2)).thenReturn(true);
        when(weekRepository.existsById(3)).thenReturn(false);
        when(dayRepository.existsById(4)).thenReturn(true);
        when(cityRepository.existsById(5)).thenReturn(false);
        when(eventRepository.existsById(6)).thenReturn(true);
        when(countryRepository.existsById(7)).thenReturn(false);

        assertThrows(DataIntegrityViolationException.class, () -> eventService.updateEvent(eventId, eventToUpdate));
    }

    @Test
    public void whenEventIdIsNotFound_updateEvent_ThrowsIllegalArgumentException() {
        int eventId = 1;
        Event event = new Event(eventId, "Old Event Name", "Old Description", 1, UUID.randomUUID());

        when(eventRepository.existsById(eventId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> eventService.updateEvent(eventId, event));
    }

    @Test
    public void whenSomeEventFieldsChanged_updateEvent_OnlyUpdatesChangedFields() {
        int eventId = 1;
        Event event = new Event(eventId, "Old Event Name", "Old Description", 120, UUID.randomUUID());
        Event eventToUpdate = new Event();
        eventToUpdate.setDescription("New event description");

        when(eventRepository.existsById(eventId)).thenReturn(true);
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        eventService.updateEvent(eventId, eventToUpdate);

        verify(eventRepository).findById(eventId);

        Event result = eventRepository.findById(eventId).get();
        Assertions.assertEquals("Old Event Name", result.getName());
        Assertions.assertEquals(eventToUpdate.getDescription(), result.getDescription());
        Assertions.assertEquals(120, result.getEvent_year());
    }
}
