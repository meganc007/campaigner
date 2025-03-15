package com.mcommings.campaigner.services.calendar;

import com.mcommings.campaigner.modules.calendar.dtos.WeekDTO;
import com.mcommings.campaigner.modules.calendar.entities.Week;
import com.mcommings.campaigner.modules.calendar.mappers.WeekMapper;
import com.mcommings.campaigner.modules.calendar.repositories.IWeekRepository;
import com.mcommings.campaigner.modules.calendar.services.WeekService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class WeekTest {

    @Mock
    private WeekMapper weekMapper;

    @Mock
    private IWeekRepository weekRepository;

    @InjectMocks
    private WeekService weekService;

    private Week entity;
    private WeekDTO dto;

    @BeforeEach
    void setUp() {
        Random random = new Random();
        entity = new Week();
        entity.setId(1);
        entity.setDescription("A fictional week.");
        entity.setFk_campaign_uuid(UUID.randomUUID());
        entity.setWeek_number(random.nextInt(100) + 1);
        entity.setFk_month(random.nextInt(100) + 1);

        dto = new WeekDTO();
        dto.setId(entity.getId());
        dto.setDescription(entity.getDescription());
        dto.setFk_campaign_uuid(entity.getFk_campaign_uuid());
        dto.setWeek_number(entity.getWeek_number());
        dto.setFk_month(entity.getFk_month());

        when(weekMapper.mapToWeekDto(entity)).thenReturn(dto);
        when(weekMapper.mapFromWeekDto(dto)).thenReturn(entity);
    }

    @Test
    public void whenThereAreWeeks_getWeeks_ReturnsWeeks() {
        when(weekRepository.findAll()).thenReturn(List.of(entity));
        List<WeekDTO> result = weekService.getWeeks();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void whenThereAreNoWeeks_getWeeks_ReturnsEmptyList() {
        when(weekRepository.findAll()).thenReturn(Collections.emptyList());

        List<WeekDTO> result = weekService.getWeeks();

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected an empty list when there are no weeks.");
    }

    @Test
    void whenThereIsAWeek_getWeek_ReturnsWeekById() {
        when(weekRepository.findById(1)).thenReturn(Optional.of(entity));

        Optional<WeekDTO> result = weekService.getWeek(1);

        assertTrue(result.isPresent());
    }

    @Test
    void whenThereIsNotAWeek_getWeek_ReturnsNothing() {
        when(weekRepository.findById(999)).thenReturn(Optional.empty());

        Optional<WeekDTO> result = weekService.getWeek(999);

        assertTrue(result.isEmpty(), "Expected empty Optional when week is not found.");
    }

    @Test
    void whenCampaignUUIDIsValid_getWeeksByCampaignUUID_ReturnsWeeks() {
        UUID campaignUUID = entity.getFk_campaign_uuid();
        when(weekRepository.findByfk_campaign_uuid(campaignUUID)).thenReturn(List.of(entity));

        List<WeekDTO> result = weekService.getWeeksByCampaignUUID(campaignUUID);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(campaignUUID, result.get(0).getFk_campaign_uuid());
    }

    @Test
    void whenCampaignUUIDIsInvalid_getWeeksByCampaignUUID_ReturnsNothing() {
        UUID campaignUUID = UUID.randomUUID();
        when(weekRepository.findByfk_campaign_uuid(campaignUUID)).thenReturn(Collections.emptyList());

        List<WeekDTO> result = weekService.getWeeksByCampaignUUID(campaignUUID);

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected an empty list when no weeks match the campaign UUID.");
    }

    @Test
    void whenWeekIsValid_saveWeek_SavesTheWeek() {
        when(weekRepository.save(entity)).thenReturn(entity);

        weekService.saveWeek(dto);

        verify(weekRepository, times(1)).save(entity);
    }

    @Test
    void whenWeekIdExists_deleteWeek_DeletesTheWeek() {
        when(weekRepository.existsById(1)).thenReturn(true);
        weekService.deleteWeek(1);
        verify(weekRepository, times(1)).deleteById(1);
    }

    @Test
    void whenWeekIdDoesNotExist_deleteWeek_ThrowsIllegalArgumentException() {
        when(weekRepository.existsById(999)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> weekService.deleteWeek(999));
    }

    @Test
    void whenDeleteWeekFails_deleteWeek_ThrowsException() {
        when(weekRepository.existsById(1)).thenReturn(true);
        doThrow(new RuntimeException("Database error")).when(weekRepository).deleteById(1);

        assertThrows(RuntimeException.class, () -> weekService.deleteWeek(1));
    }

    @Test
    void whenWeekIdIsFound_updateWeek_UpdatesTheWeek() {
        WeekDTO updateDTO = new WeekDTO();
        updateDTO.setDescription("Updated description");

        when(weekRepository.findById(1)).thenReturn(Optional.of(entity));
        when(weekRepository.existsById(1)).thenReturn(true);
        when(weekRepository.save(entity)).thenReturn(entity);
        when(weekMapper.mapToWeekDto(entity)).thenReturn(updateDTO);

        Optional<WeekDTO> result = weekService.updateWeek(1, updateDTO);

        assertTrue(result.isPresent());
        assertEquals("Updated description", result.get().getDescription());
    }

    @Test
    void whenWeekIdIsNotFound_updateWeek_ReturnsEmptyOptional() {
        WeekDTO updateDTO = new WeekDTO();
        updateDTO.setDescription("Updated description");

        when(weekRepository.findById(999)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> weekService.updateWeek(999, updateDTO));
    }
}
