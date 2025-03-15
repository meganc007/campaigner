package com.mcommings.campaigner.services.calendar;

import com.mcommings.campaigner.modules.calendar.dtos.MoonDTO;
import com.mcommings.campaigner.modules.calendar.entities.Moon;
import com.mcommings.campaigner.modules.calendar.mappers.MoonMapper;
import com.mcommings.campaigner.modules.calendar.repositories.IMoonRepository;
import com.mcommings.campaigner.modules.calendar.services.MoonService;
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
public class MoonTest {
    @Mock
    private MoonMapper moonMapper;

    @Mock
    private IMoonRepository moonRepository;

    @InjectMocks
    private MoonService moonService;

    private Moon entity;
    private MoonDTO dto;

    @BeforeEach
    void setUp() {
        Random random = new Random();
        entity = new Moon();
        entity.setId(1);
        entity.setName("Test Moon");
        entity.setDescription("A fictional moon.");
        entity.setFk_campaign_uuid(UUID.randomUUID());

        dto = new MoonDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setFk_campaign_uuid(entity.getFk_campaign_uuid());

        when(moonMapper.mapToMoonDto(entity)).thenReturn(dto);
        when(moonMapper.mapFromMoonDto(dto)).thenReturn(entity);
    }

    @Test
    public void whenThereAreMoons_getMoons_ReturnsMoons() {
        when(moonRepository.findAll()).thenReturn(List.of(entity));
        List<MoonDTO> result = moonService.getMoons();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Moon", result.get(0).getName());
    }

    @Test
    public void whenThereAreNoMoons_getMoons_ReturnsEmptyList() {
        when(moonRepository.findAll()).thenReturn(Collections.emptyList());

        List<MoonDTO> result = moonService.getMoons();

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected an empty list when there are no moons.");
    }

    @Test
    void whenThereIsAMoon_getMoon_ReturnsMoonById() {
        when(moonRepository.findById(1)).thenReturn(Optional.of(entity));

        Optional<MoonDTO> result = moonService.getMoon(1);

        assertTrue(result.isPresent());
        assertEquals("Test Moon", result.get().getName());
    }

    @Test
    void whenThereIsNotAMoon_getMoon_ReturnsNothing() {
        when(moonRepository.findById(999)).thenReturn(Optional.empty());

        Optional<MoonDTO> result = moonService.getMoon(999);

        assertTrue(result.isEmpty(), "Expected empty Optional when moon is not found.");
    }

    @Test
    void whenCampaignUUIDIsValid_getMoonsByCampaignUUID_ReturnsMoons() {
        UUID campaignUUID = entity.getFk_campaign_uuid();
        when(moonRepository.findByfk_campaign_uuid(campaignUUID)).thenReturn(List.of(entity));

        List<MoonDTO> result = moonService.getMoonsByCampaignUUID(campaignUUID);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(campaignUUID, result.get(0).getFk_campaign_uuid());
    }

    @Test
    void whenCampaignUUIDIsInvalid_getMoonsByCampaignUUID_ReturnsNothing() {
        UUID campaignUUID = UUID.randomUUID();
        when(moonRepository.findByfk_campaign_uuid(campaignUUID)).thenReturn(Collections.emptyList());

        List<MoonDTO> result = moonService.getMoonsByCampaignUUID(campaignUUID);

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected an empty list when no moons match the campaign UUID.");
    }

    @Test
    void whenMoonIsValid_saveMoon_SavesTheMoon() {
        when(moonRepository.save(entity)).thenReturn(entity);

        moonService.saveMoon(dto);

        verify(moonRepository, times(1)).save(entity);
    }

    @Test
    public void whenMoonNameIsInvalid_saveMoon_ThrowsIllegalArgumentException() {
        MoonDTO moonWithEmptyName = new MoonDTO();
        moonWithEmptyName.setId(1);
        moonWithEmptyName.setName("");
        moonWithEmptyName.setDescription("A fictional moon.");
        moonWithEmptyName.setFk_campaign_uuid(UUID.randomUUID());

        MoonDTO moonWithNullName = new MoonDTO();
        moonWithNullName.setId(1);
        moonWithNullName.setName(null);
        moonWithNullName.setDescription("A fictional moon.");
        moonWithNullName.setFk_campaign_uuid(UUID.randomUUID());


        assertThrows(IllegalArgumentException.class, () -> moonService.saveMoon(moonWithEmptyName));
        assertThrows(IllegalArgumentException.class, () -> moonService.saveMoon(moonWithNullName));
    }

    @Test
    public void whenMoonNameAlreadyExists_saveMoon_ThrowsDataIntegrityViolationException() {
        when(moonRepository.findByName(dto.getName())).thenReturn(Optional.of(entity));
        assertThrows(DataIntegrityViolationException.class, () -> moonService.saveMoon(dto));
        verify(moonRepository, times(1)).findByName(dto.getName());
        verify(moonRepository, never()).save(any(Moon.class));
    }

    @Test
    void whenMoonIdExists_deleteMoon_DeletesTheMoon() {
        when(moonRepository.existsById(1)).thenReturn(true);
        moonService.deleteMoon(1);
        verify(moonRepository, times(1)).deleteById(1);
    }

    @Test
    void whenMoonIdDoesNotExist_deleteMoon_ThrowsIllegalArgumentException() {
        when(moonRepository.existsById(999)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> moonService.deleteMoon(999));
    }

    @Test
    void whenDeleteMoonFails_deleteMoon_ThrowsException() {
        when(moonRepository.existsById(1)).thenReturn(true);
        doThrow(new RuntimeException("Database error")).when(moonRepository).deleteById(1);

        assertThrows(RuntimeException.class, () -> moonService.deleteMoon(1));
    }

    @Test
    void whenMoonIdIsFound_updateMoon_UpdatesTheMoon() {
        MoonDTO updateDTO = new MoonDTO();
        updateDTO.setName("Updated Name");

        when(moonRepository.findById(1)).thenReturn(Optional.of(entity));
        when(moonRepository.existsById(1)).thenReturn(true);
        when(moonRepository.save(entity)).thenReturn(entity);
        when(moonMapper.mapToMoonDto(entity)).thenReturn(updateDTO);

        Optional<MoonDTO> result = moonService.updateMoon(1, updateDTO);

        assertTrue(result.isPresent());
        assertEquals("Updated Name", result.get().getName());
    }

    @Test
    void whenMoonIdIsNotFound_updateMoon_ReturnsEmptyOptional() {
        MoonDTO updateDTO = new MoonDTO();
        updateDTO.setName("Updated Name");

        when(moonRepository.findById(999)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> moonService.updateMoon(999, updateDTO));
    }

    @Test
    public void whenMoonNameIsInvalid_updateMoon_ThrowsIllegalArgumentException() {
        MoonDTO updateEmptyName = new MoonDTO();
        updateEmptyName.setName("");

        MoonDTO updateNullName = new MoonDTO();
        updateNullName.setName(null);

        when(moonRepository.existsById(1)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> moonService.updateMoon(1, updateEmptyName));
        assertThrows(IllegalArgumentException.class, () -> moonService.updateMoon(1, updateNullName));
    }

    @Test
    public void whenMoonNameAlreadyExists_updateMoon_ThrowsDataIntegrityViolationException() {
        when(moonRepository.findByName(dto.getName())).thenReturn(Optional.of(entity));

        assertThrows(IllegalArgumentException.class, () -> moonService.updateMoon(entity.getId(), dto));
    }
}
