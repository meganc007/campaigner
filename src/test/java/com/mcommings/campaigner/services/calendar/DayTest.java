package com.mcommings.campaigner.services.calendar;

import com.mcommings.campaigner.modules.calendar.dtos.DayDTO;
import com.mcommings.campaigner.modules.calendar.entities.Day;
import com.mcommings.campaigner.modules.calendar.mappers.DayMapper;
import com.mcommings.campaigner.modules.calendar.repositories.IDayRepository;
import com.mcommings.campaigner.modules.calendar.services.DayService;
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
public class DayTest {

    @Mock
    private DayMapper dayMapper;

    @Mock
    private IDayRepository dayRepository;

    @InjectMocks
    private DayService dayService;

    private Day entity;
    private DayDTO dto;

    @BeforeEach
    void setUp() {
        Random random = new Random();
        entity = new Day();
        entity.setId(1);
        entity.setName("Test Day");
        entity.setDescription("A fictional day.");
        entity.setFk_campaign_uuid(UUID.randomUUID());
        entity.setFk_week(random.nextInt(100) + 1);

        dto = new DayDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setFk_campaign_uuid(entity.getFk_campaign_uuid());
        dto.setFk_week(entity.getFk_week());

        when(dayMapper.mapToDayDto(entity)).thenReturn(dto);
        when(dayMapper.mapFromDayDto(dto)).thenReturn(entity);
    }

    @Test
    public void whenThereAreDays_getDays_ReturnsDays() {
        when(dayRepository.findAll()).thenReturn(List.of(entity));
        List<DayDTO> result = dayService.getDays();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Day", result.get(0).getName());
    }

    @Test
    public void whenThereAreNoDays_getDays_ReturnsEmptyList() {
        when(dayRepository.findAll()).thenReturn(Collections.emptyList());

        List<DayDTO> result = dayService.getDays();

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected an empty list when there are no days.");
    }

    @Test
    void whenThereIsADay_getDay_ReturnsDayById() {
        when(dayRepository.findById(1)).thenReturn(Optional.of(entity));

        Optional<DayDTO> result = dayService.getDay(1);

        assertTrue(result.isPresent());
        assertEquals("Test Day", result.get().getName());
    }

    @Test
    void whenThereIsNotADay_getDay_ReturnsNothing() {
        when(dayRepository.findById(999)).thenReturn(Optional.empty());

        Optional<DayDTO> result = dayService.getDay(999);

        assertTrue(result.isEmpty(), "Expected empty Optional when day is not found.");
    }

    @Test
    void whenCampaignUUIDIsValid_getDaysByCampaignUUID_ReturnsDays() {
        UUID campaignUUID = entity.getFk_campaign_uuid();
        when(dayRepository.findByfk_campaign_uuid(campaignUUID)).thenReturn(List.of(entity));

        List<DayDTO> result = dayService.getDaysByCampaignUUID(campaignUUID);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(campaignUUID, result.get(0).getFk_campaign_uuid());
    }

    @Test
    void whenCampaignUUIDIsInvalid_getDaysByCampaignUUID_ReturnsNothing() {
        UUID campaignUUID = UUID.randomUUID();
        when(dayRepository.findByfk_campaign_uuid(campaignUUID)).thenReturn(Collections.emptyList());

        List<DayDTO> result = dayService.getDaysByCampaignUUID(campaignUUID);

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected an empty list when no days match the campaign UUID.");
    }

    @Test
    void whenDayIsValid_saveDay_SavesTheDay() {
        when(dayRepository.save(entity)).thenReturn(entity);

        dayService.saveDay(dto);

        verify(dayRepository, times(1)).save(entity);
    }

    @Test
    public void whenDayNameIsInvalid_saveDay_ThrowsIllegalArgumentException() {
        DayDTO dayWithEmptyName = new DayDTO();
        dayWithEmptyName.setId(1);
        dayWithEmptyName.setName("");
        dayWithEmptyName.setDescription("A fictional day.");
        dayWithEmptyName.setFk_campaign_uuid(UUID.randomUUID());
        dayWithEmptyName.setFk_week(1);

        DayDTO dayWithNullName = new DayDTO();
        dayWithNullName.setId(1);
        dayWithNullName.setName(null);
        dayWithNullName.setDescription("A fictional day.");
        dayWithNullName.setFk_campaign_uuid(UUID.randomUUID());
        dayWithNullName.setFk_week(1);

        assertThrows(IllegalArgumentException.class, () -> dayService.saveDay(dayWithEmptyName));
        assertThrows(IllegalArgumentException.class, () -> dayService.saveDay(dayWithNullName));
    }

    @Test
    public void whenDayNameAlreadyExists_saveDay_ThrowsDataIntegrityViolationException() {
        when(dayRepository.findByName(dto.getName())).thenReturn(Optional.of(entity));
        assertThrows(DataIntegrityViolationException.class, () -> dayService.saveDay(dto));
        verify(dayRepository, times(1)).findByName(dto.getName());
        verify(dayRepository, never()).save(any(Day.class));
    }

    @Test
    void whenDayIdExists_deleteDay_DeletesTheDay() {
        when(dayRepository.existsById(1)).thenReturn(true);
        dayService.deleteDay(1);
        verify(dayRepository, times(1)).deleteById(1);
    }

    @Test
    void whenDayIdDoesNotExist_deleteDay_ThrowsIllegalArgumentException() {
        when(dayRepository.existsById(999)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> dayService.deleteDay(999));
    }

    @Test
    void whenDeleteDayFails_deleteDay_ThrowsException() {
        when(dayRepository.existsById(1)).thenReturn(true);
        doThrow(new RuntimeException("Database error")).when(dayRepository).deleteById(1);

        assertThrows(RuntimeException.class, () -> dayService.deleteDay(1));
    }

    @Test
    void whenDayIdIsFound_updateDay_UpdatesTheDay() {
        DayDTO updateDTO = new DayDTO();
        updateDTO.setName("Updated Name");

        when(dayRepository.findById(1)).thenReturn(Optional.of(entity));
        when(dayRepository.existsById(1)).thenReturn(true);
        when(dayRepository.save(entity)).thenReturn(entity);
        when(dayMapper.mapToDayDto(entity)).thenReturn(updateDTO);

        Optional<DayDTO> result = dayService.updateDay(1, updateDTO);

        assertTrue(result.isPresent());
        assertEquals("Updated Name", result.get().getName());
    }

    @Test
    void whenDayIdIsNotFound_updateDay_ReturnsEmptyOptional() {
        DayDTO updateDTO = new DayDTO();
        updateDTO.setName("Updated Name");

        when(dayRepository.findById(999)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> dayService.updateDay(999, updateDTO));
    }

    @Test
    public void whenDayNameIsInvalid_updateDay_ThrowsIllegalArgumentException() {
        DayDTO updateEmptyName = new DayDTO();
        updateEmptyName.setName("");

        DayDTO updateNullName = new DayDTO();
        updateNullName.setName(null);

        when(dayRepository.existsById(1)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> dayService.updateDay(1, updateEmptyName));
        assertThrows(IllegalArgumentException.class, () -> dayService.updateDay(1, updateNullName));
    }

    @Test
    public void whenDayNameAlreadyExists_updateDay_ThrowsDataIntegrityViolationException() {
        when(dayRepository.findByName(dto.getName())).thenReturn(Optional.of(entity));

        assertThrows(IllegalArgumentException.class, () -> dayService.updateDay(entity.getId(), dto));
    }
}
