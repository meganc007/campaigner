package com.mcommings.campaigner.services.calendar;

import com.mcommings.campaigner.modules.calendar.dtos.SunDTO;
import com.mcommings.campaigner.modules.calendar.entities.Sun;
import com.mcommings.campaigner.modules.calendar.mappers.SunMapper;
import com.mcommings.campaigner.modules.calendar.repositories.ISunRepository;
import com.mcommings.campaigner.modules.calendar.services.SunService;
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
public class SunTest {
    @Mock
    private SunMapper sunMapper;

    @Mock
    private ISunRepository sunRepository;

    @InjectMocks
    private SunService sunService;

    private Sun entity;
    private SunDTO dto;

    @BeforeEach
    void setUp() {
        Random random = new Random();
        entity = new Sun();
        entity.setId(1);
        entity.setName("Test Sun");
        entity.setDescription("A fictional sun.");
        entity.setFk_campaign_uuid(UUID.randomUUID());

        dto = new SunDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setFk_campaign_uuid(entity.getFk_campaign_uuid());

        when(sunMapper.mapToSunDto(entity)).thenReturn(dto);
        when(sunMapper.mapFromSunDto(dto)).thenReturn(entity);
    }

    @Test
    public void whenThereAreSuns_getSuns_ReturnsSuns() {
        when(sunRepository.findAll()).thenReturn(List.of(entity));
        List<SunDTO> result = sunService.getSuns();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Sun", result.get(0).getName());
    }

    @Test
    public void whenThereAreNoSuns_getSuns_ReturnsEmptyList() {
        when(sunRepository.findAll()).thenReturn(Collections.emptyList());

        List<SunDTO> result = sunService.getSuns();

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected an empty list when there are no suns.");
    }

    @Test
    void whenThereIsASun_getSun_ReturnsSunById() {
        when(sunRepository.findById(1)).thenReturn(Optional.of(entity));

        Optional<SunDTO> result = sunService.getSun(1);

        assertTrue(result.isPresent());
        assertEquals("Test Sun", result.get().getName());
    }

    @Test
    void whenThereIsNotASun_getSun_ReturnsNothing() {
        when(sunRepository.findById(999)).thenReturn(Optional.empty());

        Optional<SunDTO> result = sunService.getSun(999);

        assertTrue(result.isEmpty(), "Expected empty Optional when sun is not found.");
    }

    @Test
    void whenCampaignUUIDIsValid_getSunsByCampaignUUID_ReturnsSuns() {
        UUID campaignUUID = entity.getFk_campaign_uuid();
        when(sunRepository.findByfk_campaign_uuid(campaignUUID)).thenReturn(List.of(entity));

        List<SunDTO> result = sunService.getSunsByCampaignUUID(campaignUUID);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(campaignUUID, result.get(0).getFk_campaign_uuid());
    }

    @Test
    void whenCampaignUUIDIsInvalid_getSunsByCampaignUUID_ReturnsNothing() {
        UUID campaignUUID = UUID.randomUUID();
        when(sunRepository.findByfk_campaign_uuid(campaignUUID)).thenReturn(Collections.emptyList());

        List<SunDTO> result = sunService.getSunsByCampaignUUID(campaignUUID);

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected an empty list when no suns match the campaign UUID.");
    }

    @Test
    void whenSunIsValid_saveSun_SavesTheSun() {
        when(sunRepository.save(entity)).thenReturn(entity);

        sunService.saveSun(dto);

        verify(sunRepository, times(1)).save(entity);
    }

    @Test
    public void whenSunNameIsInvalid_saveSun_ThrowsIllegalArgumentException() {
        SunDTO sunWithEmptyName = new SunDTO();
        sunWithEmptyName.setId(1);
        sunWithEmptyName.setName("");
        sunWithEmptyName.setDescription("A fictional sun.");
        sunWithEmptyName.setFk_campaign_uuid(UUID.randomUUID());

        SunDTO sunWithNullName = new SunDTO();
        sunWithNullName.setId(1);
        sunWithNullName.setName(null);
        sunWithNullName.setDescription("A fictional sun.");
        sunWithNullName.setFk_campaign_uuid(UUID.randomUUID());


        assertThrows(IllegalArgumentException.class, () -> sunService.saveSun(sunWithEmptyName));
        assertThrows(IllegalArgumentException.class, () -> sunService.saveSun(sunWithNullName));
    }

    @Test
    public void whenSunNameAlreadyExists_saveSun_ThrowsDataIntegrityViolationException() {
        when(sunRepository.findByName(dto.getName())).thenReturn(Optional.of(entity));
        assertThrows(DataIntegrityViolationException.class, () -> sunService.saveSun(dto));
        verify(sunRepository, times(1)).findByName(dto.getName());
        verify(sunRepository, never()).save(any(Sun.class));
    }

    @Test
    void whenSunIdExists_deleteSun_DeletesTheSun() {
        when(sunRepository.existsById(1)).thenReturn(true);
        sunService.deleteSun(1);
        verify(sunRepository, times(1)).deleteById(1);
    }

    @Test
    void whenSunIdDoesNotExist_deleteSun_ThrowsIllegalArgumentException() {
        when(sunRepository.existsById(999)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> sunService.deleteSun(999));
    }

    @Test
    void whenDeleteSunFails_deleteSun_ThrowsException() {
        when(sunRepository.existsById(1)).thenReturn(true);
        doThrow(new RuntimeException("Database error")).when(sunRepository).deleteById(1);

        assertThrows(RuntimeException.class, () -> sunService.deleteSun(1));
    }

    @Test
    void whenSunIdIsFound_updateSun_UpdatesTheSun() {
        SunDTO updateDTO = new SunDTO();
        updateDTO.setName("Updated Name");

        when(sunRepository.findById(1)).thenReturn(Optional.of(entity));
        when(sunRepository.existsById(1)).thenReturn(true);
        when(sunRepository.save(entity)).thenReturn(entity);
        when(sunMapper.mapToSunDto(entity)).thenReturn(updateDTO);

        Optional<SunDTO> result = sunService.updateSun(1, updateDTO);

        assertTrue(result.isPresent());
        assertEquals("Updated Name", result.get().getName());
    }

    @Test
    void whenSunIdIsNotFound_updateSun_ReturnsEmptyOptional() {
        SunDTO updateDTO = new SunDTO();
        updateDTO.setName("Updated Name");

        when(sunRepository.findById(999)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> sunService.updateSun(999, updateDTO));
    }

    @Test
    public void whenSunNameIsInvalid_updateSun_ThrowsIllegalArgumentException() {
        SunDTO updateEmptyName = new SunDTO();
        updateEmptyName.setName("");

        SunDTO updateNullName = new SunDTO();
        updateNullName.setName(null);

        when(sunRepository.existsById(1)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> sunService.updateSun(1, updateEmptyName));
        assertThrows(IllegalArgumentException.class, () -> sunService.updateSun(1, updateNullName));
    }

    @Test
    public void whenSunNameAlreadyExists_updateSun_ThrowsDataIntegrityViolationException() {
        when(sunRepository.findByName(dto.getName())).thenReturn(Optional.of(entity));

        assertThrows(IllegalArgumentException.class, () -> sunService.updateSun(entity.getId(), dto));
    }
}
