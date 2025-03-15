package com.mcommings.campaigner.services.calendar;

import com.mcommings.campaigner.modules.calendar.dtos.CelestialEventDTO;
import com.mcommings.campaigner.modules.calendar.entities.CelestialEvent;
import com.mcommings.campaigner.modules.calendar.mappers.CelestialEventMapper;
import com.mcommings.campaigner.modules.calendar.repositories.ICelestialEventRepository;
import com.mcommings.campaigner.modules.calendar.services.CelestialEventService;
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
public class CelestialEventTest {
    @Mock
    private CelestialEventMapper celestialEventMapper;

    @Mock
    private ICelestialEventRepository celestialEventRepository;

    @InjectMocks
    private CelestialEventService celestialEventService;

    private CelestialEvent entity;
    private CelestialEventDTO dto;

    @BeforeEach
    void setUp() {
        Random random = new Random();
        entity = new CelestialEvent();
        entity.setId(1);
        entity.setName("Test CelestialEvent");
        entity.setDescription("A fictional celestialEvent.");
        entity.setFk_campaign_uuid(UUID.randomUUID());
        entity.setFk_moon(random.nextInt(100) + 1);
        entity.setFk_sun(random.nextInt(100) + 1);
        entity.setFk_month(random.nextInt(100) + 1);
        entity.setFk_week(random.nextInt(100) + 1);
        entity.setFk_day(random.nextInt(100) + 1);
        entity.setEvent_year(random.nextInt(100) + 1);

        dto = new CelestialEventDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setFk_campaign_uuid(entity.getFk_campaign_uuid());
        dto.setFk_moon(entity.getFk_moon());
        dto.setFk_sun(entity.getFk_sun());
        dto.setFk_month(entity.getFk_month());
        dto.setFk_week(entity.getFk_week());
        dto.setFk_day(entity.getFk_day());
        dto.setEvent_year(entity.getEvent_year());

        when(celestialEventMapper.mapToCelestialEventDto(entity)).thenReturn(dto);
        when(celestialEventMapper.mapFromCelestialEventDto(dto)).thenReturn(entity);
    }

    @Test
    public void whenThereAreCelestialEvents_getCelestialEvents_ReturnsCelestialEvents() {
        when(celestialEventRepository.findAll()).thenReturn(List.of(entity));
        List<CelestialEventDTO> result = celestialEventService.getCelestialEvents();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test CelestialEvent", result.get(0).getName());
    }

    @Test
    public void whenThereAreNoCelestialEvents_getCelestialEvents_ReturnsEmptyList() {
        when(celestialEventRepository.findAll()).thenReturn(Collections.emptyList());

        List<CelestialEventDTO> result = celestialEventService.getCelestialEvents();

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected an empty list when there are no celestialEvents.");
    }

    @Test
    void whenThereIsACelestialEvent_getCelestialEvent_ReturnsCelestialEventById() {
        when(celestialEventRepository.findById(1)).thenReturn(Optional.of(entity));

        Optional<CelestialEventDTO> result = celestialEventService.getCelestialEvent(1);

        assertTrue(result.isPresent());
        assertEquals("Test CelestialEvent", result.get().getName());
    }

    @Test
    void whenThereIsNotACelestialEvent_getCelestialEvent_ReturnsNothing() {
        when(celestialEventRepository.findById(999)).thenReturn(Optional.empty());

        Optional<CelestialEventDTO> result = celestialEventService.getCelestialEvent(999);

        assertTrue(result.isEmpty(), "Expected empty Optional when celestialEvent is not found.");
    }

    @Test
    void whenCampaignUUIDIsValid_getCelestialEventsByCampaignUUID_ReturnsCelestialEvents() {
        UUID campaignUUID = entity.getFk_campaign_uuid();
        when(celestialEventRepository.findByfk_campaign_uuid(campaignUUID)).thenReturn(List.of(entity));

        List<CelestialEventDTO> result = celestialEventService.getCelestialEventsByCampaignUUID(campaignUUID);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(campaignUUID, result.get(0).getFk_campaign_uuid());
    }

    @Test
    void whenCampaignUUIDIsInvalid_getCelestialEventsByCampaignUUID_ReturnsNothing() {
        UUID campaignUUID = UUID.randomUUID();
        when(celestialEventRepository.findByfk_campaign_uuid(campaignUUID)).thenReturn(Collections.emptyList());

        List<CelestialEventDTO> result = celestialEventService.getCelestialEventsByCampaignUUID(campaignUUID);

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected an empty list when no celestialEvents match the campaign UUID.");
    }

    @Test
    void whenCelestialEventIsValid_saveCelestialEvent_SavesTheCelestialEvent() {
        when(celestialEventRepository.save(entity)).thenReturn(entity);

        celestialEventService.saveCelestialEvent(dto);

        verify(celestialEventRepository, times(1)).save(entity);
    }

    @Test
    public void whenCelestialEventNameIsInvalid_saveCelestialEvent_ThrowsIllegalArgumentException() {
        CelestialEventDTO celestialEventWithEmptyName = new CelestialEventDTO();
        celestialEventWithEmptyName.setId(1);
        celestialEventWithEmptyName.setName("");
        celestialEventWithEmptyName.setDescription("A fictional celestialEvent.");
        celestialEventWithEmptyName.setFk_campaign_uuid(UUID.randomUUID());
        celestialEventWithEmptyName.setFk_moon(1);
        celestialEventWithEmptyName.setFk_sun(1);
        celestialEventWithEmptyName.setFk_month(1);
        celestialEventWithEmptyName.setFk_week(1);
        celestialEventWithEmptyName.setFk_day(1);
        celestialEventWithEmptyName.setEvent_year(1);

        CelestialEventDTO celestialEventWithNullName = new CelestialEventDTO();
        celestialEventWithNullName.setId(1);
        celestialEventWithNullName.setName(null);
        celestialEventWithNullName.setDescription("A fictional celestialEvent.");
        celestialEventWithNullName.setFk_campaign_uuid(UUID.randomUUID());
        celestialEventWithNullName.setFk_moon(1);
        celestialEventWithNullName.setFk_sun(1);
        celestialEventWithNullName.setFk_month(1);
        celestialEventWithNullName.setFk_week(1);
        celestialEventWithNullName.setFk_day(1);
        celestialEventWithNullName.setEvent_year(1);

        assertThrows(IllegalArgumentException.class, () -> celestialEventService.saveCelestialEvent(celestialEventWithEmptyName));
        assertThrows(IllegalArgumentException.class, () -> celestialEventService.saveCelestialEvent(celestialEventWithNullName));
    }

    @Test
    public void whenCelestialEventNameAlreadyExists_saveCelestialEvent_ThrowsDataIntegrityViolationException() {
        when(celestialEventRepository.findByName(dto.getName())).thenReturn(Optional.of(entity));
        assertThrows(DataIntegrityViolationException.class, () -> celestialEventService.saveCelestialEvent(dto));
        verify(celestialEventRepository, times(1)).findByName(dto.getName());
        verify(celestialEventRepository, never()).save(any(CelestialEvent.class));
    }

    @Test
    void whenCelestialEventIdExists_deleteCelestialEvent_DeletesTheCelestialEvent() {
        when(celestialEventRepository.existsById(1)).thenReturn(true);
        celestialEventService.deleteCelestialEvent(1);
        verify(celestialEventRepository, times(1)).deleteById(1);
    }

    @Test
    void whenCelestialEventIdDoesNotExist_deleteCelestialEvent_ThrowsIllegalArgumentException() {
        when(celestialEventRepository.existsById(999)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> celestialEventService.deleteCelestialEvent(999));
    }

    @Test
    void whenDeleteCelestialEventFails_deleteCelestialEvent_ThrowsException() {
        when(celestialEventRepository.existsById(1)).thenReturn(true);
        doThrow(new RuntimeException("Database error")).when(celestialEventRepository).deleteById(1);

        assertThrows(RuntimeException.class, () -> celestialEventService.deleteCelestialEvent(1));
    }

    @Test
    void whenCelestialEventIdIsFound_updateCelestialEvent_UpdatesTheCelestialEvent() {
        CelestialEventDTO updateDTO = new CelestialEventDTO();
        updateDTO.setName("Updated Name");

        when(celestialEventRepository.findById(1)).thenReturn(Optional.of(entity));
        when(celestialEventRepository.existsById(1)).thenReturn(true);
        when(celestialEventRepository.save(entity)).thenReturn(entity);
        when(celestialEventMapper.mapToCelestialEventDto(entity)).thenReturn(updateDTO);

        Optional<CelestialEventDTO> result = celestialEventService.updateCelestialEvent(1, updateDTO);

        assertTrue(result.isPresent());
        assertEquals("Updated Name", result.get().getName());
    }

    @Test
    void whenCelestialEventIdIsNotFound_updateCelestialEvent_ReturnsEmptyOptional() {
        CelestialEventDTO updateDTO = new CelestialEventDTO();
        updateDTO.setName("Updated Name");

        when(celestialEventRepository.findById(999)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> celestialEventService.updateCelestialEvent(999, updateDTO));
    }

    @Test
    public void whenCelestialEventNameIsInvalid_updateCelestialEvent_ThrowsIllegalArgumentException() {
        CelestialEventDTO updateEmptyName = new CelestialEventDTO();
        updateEmptyName.setName("");

        CelestialEventDTO updateNullName = new CelestialEventDTO();
        updateNullName.setName(null);

        when(celestialEventRepository.existsById(1)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> celestialEventService.updateCelestialEvent(1, updateEmptyName));
        assertThrows(IllegalArgumentException.class, () -> celestialEventService.updateCelestialEvent(1, updateNullName));
    }

    @Test
    public void whenCelestialEventNameAlreadyExists_updateCelestialEvent_ThrowsDataIntegrityViolationException() {
        when(celestialEventRepository.findByName(dto.getName())).thenReturn(Optional.of(entity));

        assertThrows(IllegalArgumentException.class, () -> celestialEventService.updateCelestialEvent(entity.getId(), dto));
    }
}
