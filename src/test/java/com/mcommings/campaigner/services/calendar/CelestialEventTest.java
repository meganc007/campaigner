package com.mcommings.campaigner.services.calendar;

import com.mcommings.campaigner.models.calendar.CelestialEvent;
import com.mcommings.campaigner.repositories.calendar.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CelestialEventTest {
    @Mock 
    private ICelestialEventRepository celestialEventRepository;
    @Mock 
    private IMoonRepository moonRepository;
    @Mock 
    private ISunRepository sunRepository;
    @Mock 
    private IMonthRepository monthRepository;
    @Mock 
    private IWeekRepository weekRepository;
    @Mock 
    private IDayRepository dayRepository;
    
    @InjectMocks
    private CelestialEventService celestialEventService;
    
    @Test
    public void whenThereAreCities_getCities_ReturnsCities() {
        List<CelestialEvent> celestialEvents = new ArrayList<>();
        UUID campaign = UUID.randomUUID();
        celestialEvents.add(new CelestialEvent(1, "CelestialEvent 1", "Description 1", 1, campaign));
        celestialEvents.add(new CelestialEvent(2, "CelestialEvent 2", "Description 2", 1, campaign));
        celestialEvents.add(new CelestialEvent(3, "CelestialEvent 3", "Description 3", 1, 1, 3, 4, 5, 1, campaign));
        when(celestialEventRepository.findAll()).thenReturn(celestialEvents);

        List<CelestialEvent> result = celestialEventService.getCelestialEvents();

        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals(celestialEvents, result);
    }

    @Test
    public void whenThereAreNoCities_getCities_ReturnsNothing() {
        List<CelestialEvent> celestialEvents = new ArrayList<>();
        when(celestialEventRepository.findAll()).thenReturn(celestialEvents);

        List<CelestialEvent> result = celestialEventService.getCelestialEvents();

        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(celestialEvents, result);
    }

    @Test
    public void whenCampaignUUIDIsValid_getCelestialEventsByCampaignUUID_ReturnsCelestialEvents() {
        List<CelestialEvent> celestialEvents = new ArrayList<>();
        UUID campaign = UUID.randomUUID();
        celestialEvents.add(new CelestialEvent(1, "CelestialEvent 1", "Description 1", 1, campaign));
        celestialEvents.add(new CelestialEvent(2, "CelestialEvent 2", "Description 2", 1, campaign));
        celestialEvents.add(new CelestialEvent(3, "CelestialEvent 3", "Description 3", 1, 1, 3, 4, 5, 1, campaign));
        when(celestialEventRepository.findByfk_campaign_uuid(campaign)).thenReturn(celestialEvents);

        List<CelestialEvent> results = celestialEventService.getCelestialEventsByCampaignUUID(campaign);

        Assertions.assertEquals(3, results.size());
        Assertions.assertEquals(celestialEvents, results);
    }

    @Test
    public void whenCampaignUUIDIsInvalid_getCelestialEventsByCampaignUUID_ReturnsNothing() {
        UUID campaign = UUID.randomUUID();
        List<CelestialEvent> celestialEvents = new ArrayList<>();
        when(celestialEventRepository.findByfk_campaign_uuid(campaign)).thenReturn(celestialEvents);

        List<CelestialEvent> result = celestialEventService.getCelestialEventsByCampaignUUID(campaign);

        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(celestialEvents, result);
    }

    @Test
    public void whenCelestialEventWithNoForeignKeysIsValid_saveCelestialEvent_SavesTheCelestialEvent() {
        CelestialEvent celestialEvent = new CelestialEvent(1, "CelestialEvent 1", "Description 1", 1, UUID.randomUUID());
        when(celestialEventRepository.saveAndFlush(celestialEvent)).thenReturn(celestialEvent);

        assertDoesNotThrow(() -> celestialEventService.saveCelestialEvent(celestialEvent));

        verify(celestialEventRepository, times(1)).saveAndFlush(celestialEvent);
    }

    @Test
    public void whenCelestialEventWithForeignKeysIsValid_saveCelestialEvent_SavesTheCelestialEvent() {
        CelestialEvent celestialEvent = new CelestialEvent(1, "CelestialEvent 1", "Description 1",
                1, 2, 3, 4, 1, 1, UUID.randomUUID());

        when(moonRepository.existsById(1)).thenReturn(true);
        when(sunRepository.existsById(2)).thenReturn(true);
        when(monthRepository.existsById(3)).thenReturn(true);
        when(weekRepository.existsById(4)).thenReturn(true);
        when(dayRepository.existsById(1)).thenReturn(true);
        when(celestialEventRepository.saveAndFlush(celestialEvent)).thenReturn(celestialEvent);

        assertDoesNotThrow(() -> celestialEventService.saveCelestialEvent(celestialEvent));

        verify(celestialEventRepository, times(1)).saveAndFlush(celestialEvent);
    }

    @Test
    public void whenCelestialEventNameIsInvalid_saveCelestialEvent_ThrowsIllegalArgumentException() {
        CelestialEvent celestialEventWithEmptyName = new CelestialEvent(1, "", "Description 1", 1, UUID.randomUUID());
        CelestialEvent celestialEventWithNullName = new CelestialEvent(2, null, "Description 2", 1, UUID.randomUUID());

        assertThrows(IllegalArgumentException.class, () -> celestialEventService.saveCelestialEvent(celestialEventWithEmptyName));
        assertThrows(IllegalArgumentException.class, () -> celestialEventService.saveCelestialEvent(celestialEventWithNullName));
    }
    
    @Test
    public void whenCelestialEventHasInvalidForeignKeys_saveCelestialEvent_ThrowsDataIntegrityViolationException() {
        CelestialEvent celestialEvent = new CelestialEvent(1, "CelestialEvent 1", "Description 1",
                1, 2, 3, 4, 1, 1, UUID.randomUUID());
        when(moonRepository.existsById(1)).thenReturn(true);
        when(sunRepository.existsById(2)).thenReturn(false);
        when(monthRepository.existsById(3)).thenReturn(false);
        when(weekRepository.existsById(3)).thenReturn(true);
        when(dayRepository.existsById(1)).thenReturn(true);
        when(celestialEventRepository.saveAndFlush(celestialEvent)).thenReturn(celestialEvent);

        assertThrows(DataIntegrityViolationException.class, () -> celestialEventService.saveCelestialEvent(celestialEvent));

    }

    @Test
    public void whenCelestialEventIdExists_deleteCelestialEvent_DeletesTheCelestialEvent() {
        int celestialEventId = 1;
        when(celestialEventRepository.existsById(celestialEventId)).thenReturn(true);
        assertDoesNotThrow(() -> celestialEventService.deleteCelestialEvent(celestialEventId));
        verify(celestialEventRepository, times(1)).deleteById(celestialEventId);
    }

    @Test
    public void whenCelestialEventIdDoesNotExist_deleteCelestialEvent_ThrowsIllegalArgumentException() {
        int celestialEventId = 9000;
        when(celestialEventRepository.existsById(celestialEventId)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> celestialEventService.deleteCelestialEvent(celestialEventId));
    }
    
    @Test
    public void whenCelestialEventIdWithNoFKIsFound_updateCelestialEvent_UpdatesTheCelestialEvent() {
        int celestialEventId1 = 1;
        UUID campaign = UUID.randomUUID();

        CelestialEvent celestialEvent = new CelestialEvent(celestialEventId1, "Old CelestialEvent Name", "Old Description", 1, campaign);
        CelestialEvent celestialEventToUpdateNoFK = new CelestialEvent(celestialEventId1, "Updated CelestialEvent Name", "Updated Description", 2, campaign);

        when(celestialEventRepository.existsById(celestialEventId1)).thenReturn(true);
        when(celestialEventRepository.findById(celestialEventId1)).thenReturn(Optional.of(celestialEvent));

        celestialEventService.updateCelestialEvent(celestialEventId1, celestialEventToUpdateNoFK);

        verify(celestialEventRepository).findById(celestialEventId1);

        CelestialEvent result1 = celestialEventRepository.findById(celestialEventId1).get();
        Assertions.assertEquals(celestialEventToUpdateNoFK.getName(), result1.getName());
        Assertions.assertEquals(celestialEventToUpdateNoFK.getDescription(), result1.getDescription());
        Assertions.assertEquals(celestialEventToUpdateNoFK.getEvent_year(), result1.getEvent_year());
    }
    
    @Test
    public void whenCelestialEventIdWithValidFKIsFound_updateCelestialEvent_UpdatesTheCelestialEvent() {
        int celestialEventId = 2;
        UUID campaign = UUID.randomUUID();

        CelestialEvent celestialEvent = new CelestialEvent(celestialEventId, "Test CelestialEvent Name", "Test Description", 1, campaign);
        CelestialEvent celestialEventToUpdate = new CelestialEvent(celestialEventId, "Updated CelestialEvent Name", "Updated Description",
                1, 2, 3, 4, 5, 1, campaign);

        when(celestialEventRepository.existsById(celestialEventId)).thenReturn(true);
        when(celestialEventRepository.findById(celestialEventId)).thenReturn(Optional.of(celestialEvent));
        when(moonRepository.existsById(1)).thenReturn(true);
        when(sunRepository.existsById(2)).thenReturn(true);
        when(monthRepository.existsById(3)).thenReturn(true);
        when(weekRepository.existsById(4)).thenReturn(true);
        when(dayRepository.existsById(5)).thenReturn(true);

        celestialEventService.updateCelestialEvent(celestialEventId, celestialEventToUpdate);

        verify(celestialEventRepository).findById(celestialEventId);

        CelestialEvent result = celestialEventRepository.findById(celestialEventId).get();
        Assertions.assertEquals(celestialEventToUpdate.getName(), result.getName());
        Assertions.assertEquals(celestialEventToUpdate.getDescription(), result.getDescription());
        Assertions.assertEquals(celestialEventToUpdate.getFk_moon(), result.getFk_moon());
        Assertions.assertEquals(celestialEventToUpdate.getFk_sun(), result.getFk_sun());
        Assertions.assertEquals(celestialEventToUpdate.getFk_month(), result.getFk_month());
        Assertions.assertEquals(celestialEventToUpdate.getFk_week(), result.getFk_week());
        Assertions.assertEquals(celestialEventToUpdate.getFk_day(), result.getFk_day());
        Assertions.assertEquals(celestialEventToUpdate.getEvent_year(), result.getEvent_year());
    }

    @Test
    public void whenCelestialEventIdWithInvalidFKIsFound_updateCelestialEvent_ThrowsDataIntegrityViolationException() {
        int celestialEventId = 2;
        UUID campaign = UUID.randomUUID();

        CelestialEvent celestialEvent = new CelestialEvent(celestialEventId, "Test CelestialEvent Name", "Test Description", 1, campaign);
        CelestialEvent celestialEventToUpdate = new CelestialEvent(celestialEventId, "Updated CelestialEvent Name", "Updated Description", 1, 2, 3, 4, 5, 1, campaign);

        when(celestialEventRepository.existsById(celestialEventId)).thenReturn(true);
        when(celestialEventRepository.findById(celestialEventId)).thenReturn(Optional.of(celestialEvent));
        when(moonRepository.existsById(1)).thenReturn(true);
        when(sunRepository.existsById(2)).thenReturn(false);
        when(monthRepository.existsById(3)).thenReturn(true);
        when(weekRepository.existsById(4)).thenReturn(false);
        when(dayRepository.existsById(4)).thenReturn(true);

        assertThrows(DataIntegrityViolationException.class, () -> celestialEventService.updateCelestialEvent(celestialEventId, celestialEventToUpdate));
    }

    @Test
    public void whenCelestialEventIdIsNotFound_updateCelestialEvent_ThrowsIllegalArgumentException() {
        int celestialEventId = 1;
        CelestialEvent celestialEvent = new CelestialEvent(celestialEventId, "Old CelestialEvent Name", "Old Description", 1, UUID.randomUUID());

        when(celestialEventRepository.existsById(celestialEventId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> celestialEventService.updateCelestialEvent(celestialEventId, celestialEvent));
    }

    @Test
    public void whenSomeCelestialEventFieldsChanged_updateCelestialEvent_OnlyUpdatesChangedFields() {
        int celestialEventId = 1;
        CelestialEvent celestialEvent = new CelestialEvent(celestialEventId, "Test Celestial Event Name", "Test Description", 1, UUID.randomUUID());

        String newName = "Solar Eclipse";
        int newYear = 122;

        CelestialEvent celestialEventToUpdate = new CelestialEvent();
        celestialEventToUpdate.setName(newName);
        celestialEventToUpdate.setEvent_year(newYear);

        when(celestialEventRepository.existsById(celestialEventId)).thenReturn(true);
        when(celestialEventRepository.findById(celestialEventId)).thenReturn(Optional.of(celestialEvent));

        celestialEventService.updateCelestialEvent(celestialEventId, celestialEventToUpdate);

        verify(celestialEventRepository).findById(celestialEventId);

        CelestialEvent result = celestialEventRepository.findById(celestialEventId).get();
        Assertions.assertEquals(newName, result.getName());
        Assertions.assertEquals(celestialEvent.getDescription(), result.getDescription());
        Assertions.assertEquals(newYear, result.getEvent_year());
    }
}
