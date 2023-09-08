package com.mcommings.campaigner.services.people;

import com.mcommings.campaigner.models.people.EventPlacePerson;
import com.mcommings.campaigner.repositories.IEventRepository;
import com.mcommings.campaigner.repositories.locations.IPlaceRepository;
import com.mcommings.campaigner.repositories.people.IEventPlacePersonRepository;
import com.mcommings.campaigner.repositories.people.IPersonRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class EventPlacePersonTest {

    @Mock
    private IEventPlacePersonRepository eventPlacePersonRepository;
    @Mock
    private IEventRepository eventRepository;
    @Mock
    private IPlaceRepository placeRepository;
    @Mock
    private IPersonRepository personRepository;

    @InjectMocks
    private EventPlacePersonService eventPlacePersonService;

    @Test
    public void whenThereAreEventsPlacesPeople_getEventsPlacesPeople_ReturnsEventsPlacesPeople() {
        List<EventPlacePerson> epps = new ArrayList<>();
        epps.add(new EventPlacePerson(1, 1, 1, 1));
        epps.add(new EventPlacePerson(2, 2, 2, 2));
        epps.add(new EventPlacePerson(3, 3, 3, 3));
        when(eventPlacePersonRepository.findAll()).thenReturn(epps);

        List<EventPlacePerson> result = eventPlacePersonService.getEventsPlacesPeople();

        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals(epps, result);
    }

    @Test
    public void whenThereAreNoEventsPlacesPeople_getEventsPlacesPeople_ReturnsNothing() {
        List<EventPlacePerson> epps = new ArrayList<>();
        when(eventPlacePersonRepository.findAll()).thenReturn(epps);

        List<EventPlacePerson> result = eventPlacePersonService.getEventsPlacesPeople();

        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(epps, result);
    }

    @Test
    public void whenEventPlacePersonIsValid_saveEventPlacePerson_SavesTheEventPlacePerson() {
        EventPlacePerson epp = new EventPlacePerson(1, 2, 3, 4);

        when(eventRepository.existsById(2)).thenReturn(true);
        when(placeRepository.existsById(3)).thenReturn(true);
        when(personRepository.existsById(4)).thenReturn(true);
        when(eventPlacePersonRepository.saveAndFlush(epp)).thenReturn(epp);

        assertDoesNotThrow(() -> eventPlacePersonService.saveEventPlacePerson(epp));

        verify(eventPlacePersonRepository, times(1)).saveAndFlush(epp);
    }

    @Test
    public void whenEventPlacePersonAlreadyExists_saveEventPlacePerson_ThrowsDataIntegrityViolationException() {
        EventPlacePerson epp = new EventPlacePerson(1, 2, 3, 4);
        EventPlacePerson copy = new EventPlacePerson(2, 2, 3, 4);

        when(eventRepository.existsById(2)).thenReturn(true);
        when(placeRepository.existsById(3)).thenReturn(true);
        when(personRepository.existsById(4)).thenReturn(true);

        when(eventPlacePersonRepository.saveAndFlush(epp)).thenReturn(epp);
        when(eventPlacePersonRepository.saveAndFlush(copy)).thenThrow(DataIntegrityViolationException.class);

        assertDoesNotThrow(() -> eventPlacePersonService.saveEventPlacePerson(epp));
        assertThrows(DataIntegrityViolationException.class, () -> eventPlacePersonService.saveEventPlacePerson(copy));
    }

    @Test
    public void whenEventPlacePersonIdExists_deleteEventPlacePerson_DeletesTheEventPlacePerson() {
        int eventPlacePersonId = 1;
        when(eventPlacePersonRepository.existsById(eventPlacePersonId)).thenReturn(true);
        assertDoesNotThrow(() -> eventPlacePersonService.deleteEventPlacePerson(eventPlacePersonId));
        verify(eventPlacePersonRepository, times(1)).deleteById(eventPlacePersonId);
    }

    @Test
    public void whenEventPlacePersonIdDoesNotExist_deleteEventPlacePerson_ThrowsIllegalArgumentException() {
        int eventPlacePersonId = 9000;
        when(eventPlacePersonRepository.existsById(eventPlacePersonId)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> eventPlacePersonService.deleteEventPlacePerson(eventPlacePersonId));
    }

    //TODO: test delete when EventPlacePerson is a fk

    @Test
    public void whenEventPlacePersonIdWithValidFKIsFound_updateEventPlacePerson_UpdatesTheEventPlacePerson() {
        int eppId = 1;

        EventPlacePerson eventPlacePerson = new EventPlacePerson(eppId, 2, 3, 4);
        EventPlacePerson update = new EventPlacePerson(eppId, 5, 6, 7);

        when(eventPlacePersonRepository.existsById(eppId)).thenReturn(true);
        when(eventPlacePersonRepository.findById(eppId)).thenReturn(Optional.of(eventPlacePerson));
        when(eventRepository.existsById(2)).thenReturn(true);
        when(placeRepository.existsById(3)).thenReturn(true);
        when(personRepository.existsById(4)).thenReturn(true);

        when(eventRepository.existsById(5)).thenReturn(true);
        when(placeRepository.existsById(6)).thenReturn(true);
        when(personRepository.existsById(7)).thenReturn(true);

        eventPlacePersonService.updateEventPlacePerson(eppId, update);

        verify(eventPlacePersonRepository).findById(eppId);

        EventPlacePerson result = eventPlacePersonRepository.findById(eppId).get();
        Assertions.assertEquals(update.getId(), result.getId());
        Assertions.assertEquals(update.getFk_event(), result.getFk_event());
        Assertions.assertEquals(update.getFk_place(), result.getFk_place());
        Assertions.assertEquals(update.getFk_person(), result.getFk_person());
    }

    @Test
    public void whenEventPlacePersonIdWithInvalidFKIsFound_updateEventPlacePerson_ThrowsDataIntegrityViolationException() {
        int eppId = 1;

        EventPlacePerson eventPlacePerson = new EventPlacePerson(eppId, 2, 3, 4);
        EventPlacePerson update = new EventPlacePerson(eppId, 5, 6, 7);

        when(eventPlacePersonRepository.existsById(eppId)).thenReturn(true);
        when(eventPlacePersonRepository.findById(eppId)).thenReturn(Optional.of(eventPlacePerson));
        when(eventRepository.existsById(2)).thenReturn(true);
        when(placeRepository.existsById(3)).thenReturn(false);
        when(personRepository.existsById(4)).thenReturn(false);

        when(eventRepository.existsById(5)).thenReturn(true);
        when(placeRepository.existsById(6)).thenReturn(false);
        when(personRepository.existsById(7)).thenReturn(false);

        assertThrows(DataIntegrityViolationException.class, () -> eventPlacePersonService.updateEventPlacePerson(eppId, update));
    }

    @Test
    public void whenEventPlacePersonIdIsNotFound_updateEventPlacePerson_ThrowsIllegalArgumentException() {
        int eppId = 1;
        EventPlacePerson eventPlacePerson = new EventPlacePerson(eppId, 2, 3, 4);

        when(eventPlacePersonRepository.existsById(eppId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> eventPlacePersonService.updateEventPlacePerson(eppId, eventPlacePerson));
    }
}
