package com.mcommings.campaigner.services.calendar;

import com.mcommings.campaigner.modules.calendar.dtos.MonthDTO;
import com.mcommings.campaigner.modules.calendar.entities.Month;
import com.mcommings.campaigner.modules.calendar.mappers.MonthMapper;
import com.mcommings.campaigner.modules.calendar.repositories.IMonthRepository;
import com.mcommings.campaigner.modules.calendar.services.MonthService;
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
public class MonthTest {
    @Mock
    private MonthMapper monthMapper;

    @Mock
    private IMonthRepository monthRepository;

    @InjectMocks
    private MonthService monthService;

    private Month entity;
    private MonthDTO dto;

    @BeforeEach
    void setUp() {
        Random random = new Random();
        entity = new Month();
        entity.setId(1);
        entity.setName("Test Month");
        entity.setDescription("A fictional month.");
        entity.setFk_campaign_uuid(UUID.randomUUID());
        entity.setSeason("A random season");

        dto = new MonthDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setFk_campaign_uuid(entity.getFk_campaign_uuid());
        dto.setSeason(entity.getSeason());

        when(monthMapper.mapToMonthDto(entity)).thenReturn(dto);
        when(monthMapper.mapFromMonthDto(dto)).thenReturn(entity);
    }

    @Test
    public void whenThereAreMonths_getMonths_ReturnsMonths() {
        when(monthRepository.findAll()).thenReturn(List.of(entity));
        List<MonthDTO> result = monthService.getMonths();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Month", result.get(0).getName());
    }

    @Test
    public void whenThereAreNoMonths_getMonths_ReturnsEmptyList() {
        when(monthRepository.findAll()).thenReturn(Collections.emptyList());

        List<MonthDTO> result = monthService.getMonths();

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected an empty list when there are no months.");
    }

    @Test
    void whenThereIsAMonth_getMonth_ReturnsMonthById() {
        when(monthRepository.findById(1)).thenReturn(Optional.of(entity));

        Optional<MonthDTO> result = monthService.getMonth(1);

        assertTrue(result.isPresent());
        assertEquals("Test Month", result.get().getName());
    }

    @Test
    void whenThereIsNotAMonth_getMonth_ReturnsNothing() {
        when(monthRepository.findById(999)).thenReturn(Optional.empty());

        Optional<MonthDTO> result = monthService.getMonth(999);

        assertTrue(result.isEmpty(), "Expected empty Optional when month is not found.");
    }

    @Test
    void whenCampaignUUIDIsValid_getMonthsByCampaignUUID_ReturnsMonths() {
        UUID campaignUUID = entity.getFk_campaign_uuid();
        when(monthRepository.findByfk_campaign_uuid(campaignUUID)).thenReturn(List.of(entity));

        List<MonthDTO> result = monthService.getMonthsByCampaignUUID(campaignUUID);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(campaignUUID, result.get(0).getFk_campaign_uuid());
    }

    @Test
    void whenCampaignUUIDIsInvalid_getMonthsByCampaignUUID_ReturnsNothing() {
        UUID campaignUUID = UUID.randomUUID();
        when(monthRepository.findByfk_campaign_uuid(campaignUUID)).thenReturn(Collections.emptyList());

        List<MonthDTO> result = monthService.getMonthsByCampaignUUID(campaignUUID);

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected an empty list when no months match the campaign UUID.");
    }

    @Test
    void whenMonthIsValid_saveMonth_SavesTheMonth() {
        when(monthRepository.save(entity)).thenReturn(entity);

        monthService.saveMonth(dto);

        verify(monthRepository, times(1)).save(entity);
    }

    @Test
    public void whenMonthNameIsInvalid_saveMonth_ThrowsIllegalArgumentException() {
        MonthDTO monthWithEmptyName = new MonthDTO();
        monthWithEmptyName.setId(1);
        monthWithEmptyName.setName("");
        monthWithEmptyName.setDescription("A fictional month.");
        monthWithEmptyName.setFk_campaign_uuid(UUID.randomUUID());
        monthWithEmptyName.setSeason("Random season");

        MonthDTO monthWithNullName = new MonthDTO();
        monthWithNullName.setId(1);
        monthWithNullName.setName(null);
        monthWithNullName.setDescription("A fictional month.");
        monthWithNullName.setFk_campaign_uuid(UUID.randomUUID());
        monthWithNullName.setSeason("A season");

        assertThrows(IllegalArgumentException.class, () -> monthService.saveMonth(monthWithEmptyName));
        assertThrows(IllegalArgumentException.class, () -> monthService.saveMonth(monthWithNullName));
    }

    @Test
    public void whenMonthNameAlreadyExists_saveMonth_ThrowsDataIntegrityViolationException() {
        when(monthRepository.findByName(dto.getName())).thenReturn(Optional.of(entity));
        assertThrows(DataIntegrityViolationException.class, () -> monthService.saveMonth(dto));
        verify(monthRepository, times(1)).findByName(dto.getName());
        verify(monthRepository, never()).save(any(Month.class));
    }

    @Test
    void whenMonthIdExists_deleteMonth_DeletesTheMonth() {
        when(monthRepository.existsById(1)).thenReturn(true);
        monthService.deleteMonth(1);
        verify(monthRepository, times(1)).deleteById(1);
    }

    @Test
    void whenMonthIdDoesNotExist_deleteMonth_ThrowsIllegalArgumentException() {
        when(monthRepository.existsById(999)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> monthService.deleteMonth(999));
    }

    @Test
    void whenDeleteMonthFails_deleteMonth_ThrowsException() {
        when(monthRepository.existsById(1)).thenReturn(true);
        doThrow(new RuntimeException("Database error")).when(monthRepository).deleteById(1);

        assertThrows(RuntimeException.class, () -> monthService.deleteMonth(1));
    }

    @Test
    void whenMonthIdIsFound_updateMonth_UpdatesTheMonth() {
        MonthDTO updateDTO = new MonthDTO();
        updateDTO.setName("Updated Name");

        when(monthRepository.findById(1)).thenReturn(Optional.of(entity));
        when(monthRepository.existsById(1)).thenReturn(true);
        when(monthRepository.save(entity)).thenReturn(entity);
        when(monthMapper.mapToMonthDto(entity)).thenReturn(updateDTO);

        Optional<MonthDTO> result = monthService.updateMonth(1, updateDTO);

        assertTrue(result.isPresent());
        assertEquals("Updated Name", result.get().getName());
    }

    @Test
    void whenMonthIdIsNotFound_updateMonth_ReturnsEmptyOptional() {
        MonthDTO updateDTO = new MonthDTO();
        updateDTO.setName("Updated Name");

        when(monthRepository.findById(999)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> monthService.updateMonth(999, updateDTO));
    }

    @Test
    public void whenMonthNameIsInvalid_updateMonth_ThrowsIllegalArgumentException() {
        MonthDTO updateEmptyName = new MonthDTO();
        updateEmptyName.setName("");

        MonthDTO updateNullName = new MonthDTO();
        updateNullName.setName(null);

        when(monthRepository.existsById(1)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> monthService.updateMonth(1, updateEmptyName));
        assertThrows(IllegalArgumentException.class, () -> monthService.updateMonth(1, updateNullName));
    }

    @Test
    public void whenMonthNameAlreadyExists_updateMonth_ThrowsDataIntegrityViolationException() {
        when(monthRepository.findByName(dto.getName())).thenReturn(Optional.of(entity));

        assertThrows(IllegalArgumentException.class, () -> monthService.updateMonth(entity.getId(), dto));
    }
}
