package com.mcommings.campaigner.services.common;

import com.mcommings.campaigner.modules.common.dtos.EventDTO;
import com.mcommings.campaigner.modules.common.entities.Event;
import com.mcommings.campaigner.modules.common.mappers.EventMapper;
import com.mcommings.campaigner.modules.common.repositories.IEventRepository;
import com.mcommings.campaigner.modules.common.services.EventService;
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
public class EventTest {

    @Mock
    private EventMapper eventMapper;

    @Mock
    private IEventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    private Event entity;
    private EventDTO dto;

    @BeforeEach
    void setUp() {
        Random random = new Random();
        entity = new Event();
        entity.setId(1);
        entity.setName("Test Event");
        entity.setDescription("A fictional event.");
        entity.setFk_campaign_uuid(UUID.randomUUID());
        entity.setEventYear(random.nextInt(100) + 1);
        entity.setFk_month(random.nextInt(100) + 1);
        entity.setFk_week(random.nextInt(100) + 1);
        entity.setFk_day(random.nextInt(100) + 1);
        entity.setFk_city(random.nextInt(100) + 1);
        entity.setFk_continent(random.nextInt(100) + 1);
        entity.setFk_country(random.nextInt(100) + 1);

        dto = new EventDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setFk_campaign_uuid(entity.getFk_campaign_uuid());
        dto.setEventYear(entity.getEventYear());
        dto.setFk_month(entity.getFk_month());
        dto.setFk_week(entity.getFk_week());
        dto.setFk_day(entity.getFk_day());
        dto.setFk_city(entity.getFk_city());
        dto.setFk_continent(entity.getFk_continent());
        dto.setFk_country(entity.getFk_country());

        when(eventMapper.mapToEventDto(entity)).thenReturn(dto);
        when(eventMapper.mapFromEventDto(dto)).thenReturn(entity);
    }

    @Test
    public void whenThereAreEvents_getEvents_ReturnsEvents() {
        when(eventRepository.findAll()).thenReturn(List.of(entity));
        List<EventDTO> result = eventService.getEvents();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Event", result.get(0).getName());
    }

    @Test
    public void whenThereAreNoEvents_getEvents_ReturnsEmptyList() {
        when(eventRepository.findAll()).thenReturn(Collections.emptyList());

        List<EventDTO> result = eventService.getEvents();

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected an empty list when there are no events.");
    }

    @Test
    void whenThereIsAEvent_getEvent_ReturnsEventById() {
        when(eventRepository.findById(1)).thenReturn(Optional.of(entity));

        Optional<EventDTO> result = eventService.getEvent(1);

        assertTrue(result.isPresent());
        assertEquals("Test Event", result.get().getName());
    }

    @Test
    void whenThereIsNotAEvent_getEvent_ReturnsNothing() {
        when(eventRepository.findById(999)).thenReturn(Optional.empty());

        Optional<EventDTO> result = eventService.getEvent(999);

        assertTrue(result.isEmpty(), "Expected empty Optional when city is not found.");
    }

    @Test
    void whenCampaignUUIDIsValid_getEventsByCampaignUUID_ReturnsEvents() {
        UUID campaignUUID = entity.getFk_campaign_uuid();
        when(eventRepository.findByfk_campaign_uuid(campaignUUID)).thenReturn(List.of(entity));

        List<EventDTO> result = eventService.getEventsByCampaignUUID(campaignUUID);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(campaignUUID, result.get(0).getFk_campaign_uuid());
    }

    @Test
    void whenCampaignUUIDIsInvalid_getEventsByCampaignUUID_ReturnsNothing() {
        UUID campaignUUID = UUID.randomUUID();
        when(eventRepository.findByfk_campaign_uuid(campaignUUID)).thenReturn(Collections.emptyList());

        List<EventDTO> result = eventService.getEventsByCampaignUUID(campaignUUID);

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected an empty list when no events match the campaign UUID.");
    }

    @Test
    void whenEventIsValid_saveEvent_SavesTheEvent() {
        when(eventRepository.save(entity)).thenReturn(entity);

        eventService.saveEvent(dto);

        verify(eventRepository, times(1)).save(entity);
    }

    @Test
    public void whenEventNameIsInvalid_saveEvent_ThrowsIllegalArgumentException() {
        EventDTO eventWithEmptyName = new EventDTO();
        eventWithEmptyName.setId(1);
        eventWithEmptyName.setName("");
        eventWithEmptyName.setDescription("A fictional event.");
        eventWithEmptyName.setFk_campaign_uuid(UUID.randomUUID());
        eventWithEmptyName.setEventYear(1);
        eventWithEmptyName.setFk_month(1);
        eventWithEmptyName.setFk_week(1);
        eventWithEmptyName.setFk_day(1);
        eventWithEmptyName.setFk_city(1);
        eventWithEmptyName.setFk_continent(1);
        eventWithEmptyName.setFk_country(1);

        EventDTO eventWithNullName = new EventDTO();
        eventWithNullName.setId(1);
        eventWithNullName.setName(null);
        eventWithNullName.setDescription("A fictional event.");
        eventWithNullName.setFk_campaign_uuid(UUID.randomUUID());
        eventWithNullName.setEventYear(1);
        eventWithNullName.setFk_month(1);
        eventWithNullName.setFk_week(1);
        eventWithNullName.setFk_day(1);
        eventWithNullName.setFk_city(1);
        eventWithNullName.setFk_continent(1);
        eventWithNullName.setFk_country(1);

        assertThrows(IllegalArgumentException.class, () -> eventService.saveEvent(eventWithEmptyName));
        assertThrows(IllegalArgumentException.class, () -> eventService.saveEvent(eventWithNullName));
    }

    @Test
    public void whenEventNameAlreadyExists_saveEvent_ThrowsDataIntegrityViolationException() {
        when(eventRepository.findByName(dto.getName())).thenReturn(Optional.of(entity));
        assertThrows(DataIntegrityViolationException.class, () -> eventService.saveEvent(dto));
        verify(eventRepository, times(1)).findByName(dto.getName());
        verify(eventRepository, never()).save(any(Event.class));
    }

    @Test
    void whenEventIdExists_deleteEvent_DeletesTheEvent() {
        when(eventRepository.existsById(1)).thenReturn(true);
        eventService.deleteEvent(1);
        verify(eventRepository, times(1)).deleteById(1);
    }

    @Test
    void whenEventIdDoesNotExist_deleteEvent_ThrowsIllegalArgumentException() {
        when(eventRepository.existsById(999)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> eventService.deleteEvent(999));
    }

    @Test
    void whenDeleteEventFails_deleteEvent_ThrowsException() {
        when(eventRepository.existsById(1)).thenReturn(true);
        doThrow(new RuntimeException("Database error")).when(eventRepository).deleteById(1);

        assertThrows(RuntimeException.class, () -> eventService.deleteEvent(1));
    }

    @Test
    void whenEventIdIsFound_updateEvent_UpdatesTheEvent() {
        EventDTO updateDTO = new EventDTO();
        updateDTO.setName("Updated Name");

        when(eventRepository.findById(1)).thenReturn(Optional.of(entity));
        when(eventRepository.existsById(1)).thenReturn(true);
        when(eventRepository.save(entity)).thenReturn(entity);
        when(eventMapper.mapToEventDto(entity)).thenReturn(updateDTO);

        Optional<EventDTO> result = eventService.updateEvent(1, updateDTO);

        assertTrue(result.isPresent());
        assertEquals("Updated Name", result.get().getName());
    }

    @Test
    void whenEventIdIsNotFound_updateEvent_ReturnsEmptyOptional() {
        EventDTO updateDTO = new EventDTO();
        updateDTO.setName("Updated Name");

        when(eventRepository.findById(999)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> eventService.updateEvent(999, updateDTO));
    }

    @Test
    public void whenEventNameIsInvalid_updateEvent_ThrowsIllegalArgumentException() {
        EventDTO updateEmptyName = new EventDTO();
        updateEmptyName.setName("");

        EventDTO updateNullName = new EventDTO();
        updateNullName.setName(null);

        when(eventRepository.existsById(1)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> eventService.updateEvent(1, updateEmptyName));
        assertThrows(IllegalArgumentException.class, () -> eventService.updateEvent(1, updateNullName));
    }

    @Test
    public void whenEventNameAlreadyExists_updateEvent_ThrowsDataIntegrityViolationException() {
        when(eventRepository.findByName(dto.getName())).thenReturn(Optional.of(entity));

        assertThrows(IllegalArgumentException.class, () -> eventService.updateEvent(entity.getId(), dto));
    }
}
