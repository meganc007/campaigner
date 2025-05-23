package com.mcommings.campaigner.services.people;


import com.mcommings.campaigner.modules.people.dtos.EventPlacePersonDTO;
import com.mcommings.campaigner.modules.people.entities.EventPlacePerson;
import com.mcommings.campaigner.modules.people.mappers.EventPlacePersonMapper;
import com.mcommings.campaigner.modules.people.repositories.IEventPlacePersonRepository;
import com.mcommings.campaigner.modules.people.services.EventPlacePersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class EventPlacePersonTest {

    @Mock
    private EventPlacePersonMapper eventPlacePersonMapper;

    @Mock
    private IEventPlacePersonRepository eventPlacePersonRepository;

    @InjectMocks
    private EventPlacePersonService eventPlacePersonService;

    private EventPlacePerson entity;
    private EventPlacePersonDTO dto;

    @BeforeEach
    void setUp() {
        Random random = new Random();
        entity = new EventPlacePerson();
        entity.setId(1);
        entity.setFk_campaign_uuid(UUID.randomUUID());
        entity.setFk_event(random.nextInt(100) + 1);
        entity.setFk_person(random.nextInt(100) + 1);
        entity.setFk_place(random.nextInt(100) + 1);

        dto = new EventPlacePersonDTO();
        dto.setId(entity.getId());
        dto.setFk_campaign_uuid(entity.getFk_campaign_uuid());
        dto.setFk_event(entity.getFk_event());
        dto.setFk_person(entity.getFk_person());
        dto.setFk_place(entity.getFk_place());

        when(eventPlacePersonMapper.mapToEventPlacePersonDto(entity)).thenReturn(dto);
        when(eventPlacePersonMapper.mapFromEventPlacePersonDto(dto)).thenReturn(entity);
    }

    @Test
    public void whenThereAreEventsPlacesPeople_getEventsPlacesPeople_ReturnsEventsPlacesPeople() {
        when(eventPlacePersonRepository.findAll()).thenReturn(List.of(entity));
        List<EventPlacePersonDTO> result = eventPlacePersonService.getEventsPlacesPeople();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
    }

    @Test
    public void whenThereAreNoEventsPlacesPeople_getEventsPlacesPeople_ReturnsNothing() {
        when(eventPlacePersonRepository.findAll()).thenReturn(Collections.emptyList());

        List<EventPlacePersonDTO> result = eventPlacePersonService.getEventsPlacesPeople();

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected an empty list when there are no eventsPlacesPeople.");
    }

    @Test
    public void whenThereIsAEventPlacePerson_getEventPlacePerson_ReturnsEventPlacePerson() {
        when(eventPlacePersonRepository.findById(1)).thenReturn(Optional.of(entity));

        Optional<EventPlacePersonDTO> result = eventPlacePersonService.getEventPlacePerson(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
    }

    @Test
    public void whenThereIsNotAEventPlacePerson_getEventPlacePerson_ReturnsEventPlacePerson() {
        when(eventPlacePersonRepository.findById(999)).thenReturn(Optional.empty());

        Optional<EventPlacePersonDTO> result = eventPlacePersonService.getEventPlacePerson(999);

        assertTrue(result.isEmpty(), "Expected empty Optional when eventPlacePerson is not found.");
    }

    @Test
    public void whenCampaignUUIDIsValid_getEventsPlacesPeopleByCampaignUUID_ReturnsEventsPlacesPeople() {
        UUID campaignUUID = entity.getFk_campaign_uuid();
        when(eventPlacePersonRepository.findByfk_campaign_uuid(campaignUUID)).thenReturn(List.of(entity));

        List<EventPlacePersonDTO> result = eventPlacePersonService.getEventsPlacesPeopleByCampaignUUID(campaignUUID);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(campaignUUID, result.get(0).getFk_campaign_uuid());
    }

    @Test
    public void whenCampaignUUIDIsInvalid_getEventsPlacesPeopleByCampaignUUID_ReturnsNothing() {
        UUID campaignUUID = UUID.randomUUID();
        when(eventPlacePersonRepository.findByfk_campaign_uuid(campaignUUID)).thenReturn(Collections.emptyList());

        List<EventPlacePersonDTO> result = eventPlacePersonService.getEventsPlacesPeopleByCampaignUUID(campaignUUID);

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected an empty list when no eventsPlacesPeople match the campaign UUID.");
    }

    @Test
    public void whenEventPlacePersonIsValid_saveEventPlacePerson_SavesTheEventPlacePerson() {
        when(eventPlacePersonRepository.save(entity)).thenReturn(entity);

        eventPlacePersonService.saveEventPlacePerson(dto);

        verify(eventPlacePersonRepository, times(1)).save(entity);
    }

    @Test
    public void whenEventPlacePersonAlreadyExists_saveEventPlacePerson_ThrowsDataIntegrityViolationException() {
        when(eventPlacePersonRepository.eventPlacePersonExists(eventPlacePersonMapper.mapFromEventPlacePersonDto(dto))).thenReturn(Optional.of(entity));
        assertThrows(DataIntegrityViolationException.class, () -> eventPlacePersonService.saveEventPlacePerson(dto));
        verify(eventPlacePersonRepository, times(1)).eventPlacePersonExists(eventPlacePersonMapper.mapFromEventPlacePersonDto(dto));
        verify(eventPlacePersonRepository, never()).save(any(EventPlacePerson.class));
    }

    @Test
    public void whenEventPlacePersonIdExists_deleteEventPlacePerson_DeletesTheEventPlacePerson() {
        when(eventPlacePersonRepository.existsById(1)).thenReturn(true);
        eventPlacePersonService.deleteEventPlacePerson(1);
        verify(eventPlacePersonRepository, times(1)).deleteById(1);
    }

    @Test
    public void whenEventPlacePersonIdDoesNotExist_deleteEventPlacePerson_ThrowsIllegalArgumentException() {
        when(eventPlacePersonRepository.existsById(999)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> eventPlacePersonService.deleteEventPlacePerson(999));
    }

    @Test
    public void whenDeleteEventPlacePersonFails_deleteEventPlacePerson_ThrowsException() {
        when(eventPlacePersonRepository.existsById(1)).thenReturn(true);
        doThrow(new RuntimeException("Database error")).when(eventPlacePersonRepository).deleteById(1);

        assertThrows(RuntimeException.class, () -> eventPlacePersonService.deleteEventPlacePerson(1));
    }

    @Test
    public void whenEventPlacePersonIdIsFound_updateEventPlacePerson_UpdatesTheEventPlacePerson() {
        EventPlacePersonDTO updateDTO = new EventPlacePersonDTO();
        updateDTO.setFk_person(3);

        when(eventPlacePersonRepository.findById(1)).thenReturn(Optional.of(entity));
        when(eventPlacePersonRepository.existsById(1)).thenReturn(true);
        when(eventPlacePersonRepository.save(entity)).thenReturn(entity);
        when(eventPlacePersonMapper.mapToEventPlacePersonDto(entity)).thenReturn(updateDTO);

        Optional<EventPlacePersonDTO> result = eventPlacePersonService.updateEventPlacePerson(1, updateDTO);

        assertTrue(result.isPresent());
        assertEquals(3, result.get().getFk_person());
    }

    @Test
    public void whenEventPlacePersonIdIsNotFound_updateEventPlacePerson_ReturnsEmptyOptional() {
        EventPlacePersonDTO updateDTO = new EventPlacePersonDTO();
        updateDTO.setFk_event(22);

        when(eventPlacePersonRepository.findById(999)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> eventPlacePersonService.updateEventPlacePerson(999, updateDTO));
    }

    @Test
    public void whenEventPlacePersonAlreadyExists_updateEventPlacePerson_ThrowsDataIntegrityViolationException() {
        when(eventPlacePersonRepository.eventPlacePersonExists(eventPlacePersonMapper.mapFromEventPlacePersonDto(dto))).thenReturn(Optional.of(entity));

        assertThrows(IllegalArgumentException.class, () -> eventPlacePersonService.updateEventPlacePerson(entity.getId(), dto));
    }
}
