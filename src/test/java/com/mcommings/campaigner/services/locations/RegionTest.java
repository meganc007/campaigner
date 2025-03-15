package com.mcommings.campaigner.services.locations;

import com.mcommings.campaigner.locations.dtos.RegionDTO;
import com.mcommings.campaigner.locations.entities.Region;
import com.mcommings.campaigner.locations.mappers.RegionMapper;
import com.mcommings.campaigner.locations.repositories.IRegionRepository;
import com.mcommings.campaigner.locations.services.RegionService;
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
public class RegionTest {
    @Mock
    private RegionMapper regionMapper;

    @Mock
    private IRegionRepository regionRepository;

    @InjectMocks
    private RegionService regionService;

    private Region entity;
    private RegionDTO dto;

    @BeforeEach
    void setUp() {
        Random random = new Random();
        entity = new Region();
        entity.setId(1);
        entity.setName("Test Region");
        entity.setDescription("A fictional region.");
        entity.setFk_campaign_uuid(UUID.randomUUID());
        entity.setFk_country(random.nextInt(100) + 1);
        entity.setFk_climate(random.nextInt(100) + 1);

        dto = new RegionDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setFk_campaign_uuid(entity.getFk_campaign_uuid());
        dto.setFk_country(entity.getFk_country());
        dto.setFk_climate(entity.getFk_climate());

        when(regionMapper.mapToRegionDto(entity)).thenReturn(dto);
        when(regionMapper.mapFromRegionDto(dto)).thenReturn(entity);
    }

    @Test
    public void whenThereAreRegions_getRegions_ReturnsRegions() {
        when(regionRepository.findAll()).thenReturn(List.of(entity));
        List<RegionDTO> result = regionService.getRegions();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Region", result.get(0).getName());
    }

    @Test
    public void whenThereAreNoRegions_getRegions_ReturnsEmptyList() {
        when(regionRepository.findAll()).thenReturn(Collections.emptyList());

        List<RegionDTO> result = regionService.getRegions();

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected an empty list when there are no regions.");
    }

    @Test
    void whenThereIsARegion_getRegion_ReturnsRegionById() {
        when(regionRepository.findById(1)).thenReturn(Optional.of(entity));

        Optional<RegionDTO> result = regionService.getRegion(1);

        assertTrue(result.isPresent());
        assertEquals("Test Region", result.get().getName());
    }

    @Test
    void whenThereIsNotARegion_getRegion_ReturnsNothing() {
        when(regionRepository.findById(999)).thenReturn(Optional.empty());

        Optional<RegionDTO> result = regionService.getRegion(999);

        assertTrue(result.isEmpty(), "Expected empty Optional when region is not found.");
    }

    @Test
    void whenCampaignUUIDIsValid_getRegionsByCampaignUUID_ReturnsRegions() {
        UUID campaignUUID = entity.getFk_campaign_uuid();
        when(regionRepository.findByfk_campaign_uuid(campaignUUID)).thenReturn(List.of(entity));

        List<RegionDTO> result = regionService.getRegionsByCampaignUUID(campaignUUID);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(campaignUUID, result.get(0).getFk_campaign_uuid());
    }

    @Test
    void whenCampaignUUIDIsInvalid_getRegionsByCampaignUUID_ReturnsNothing() {
        UUID campaignUUID = UUID.randomUUID();
        when(regionRepository.findByfk_campaign_uuid(campaignUUID)).thenReturn(Collections.emptyList());

        List<RegionDTO> result = regionService.getRegionsByCampaignUUID(campaignUUID);

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected an empty list when no regions match the campaign UUID.");
    }

    @Test
    void whenRegionIsValid_saveRegion_SavesTheRegion() {
        when(regionRepository.save(entity)).thenReturn(entity);

        regionService.saveRegion(dto);

        verify(regionRepository, times(1)).save(entity);
    }

    @Test
    public void whenRegionNameIsInvalid_saveRegion_ThrowsIllegalArgumentException() {
        RegionDTO regionWithEmptyName = new RegionDTO();
        regionWithEmptyName.setId(1);
        regionWithEmptyName.setName("");
        regionWithEmptyName.setDescription("A fictional region.");
        regionWithEmptyName.setFk_campaign_uuid(UUID.randomUUID());
        regionWithEmptyName.setFk_country(1);
        regionWithEmptyName.setFk_climate(1);

        RegionDTO regionWithNullName = new RegionDTO();
        regionWithNullName.setId(1);
        regionWithNullName.setName(null);
        regionWithNullName.setDescription("A fictional region.");
        regionWithNullName.setFk_campaign_uuid(UUID.randomUUID());
        regionWithNullName.setFk_country(1);
        regionWithNullName.setFk_climate(1);

        assertThrows(IllegalArgumentException.class, () -> regionService.saveRegion(regionWithEmptyName));
        assertThrows(IllegalArgumentException.class, () -> regionService.saveRegion(regionWithNullName));
    }

    @Test
    public void whenRegionNameAlreadyExists_saveRegion_ThrowsDataIntegrityViolationException() {
        when(regionRepository.findByName(dto.getName())).thenReturn(Optional.of(entity));
        assertThrows(DataIntegrityViolationException.class, () -> regionService.saveRegion(dto));
        verify(regionRepository, times(1)).findByName(dto.getName());
        verify(regionRepository, never()).save(any(Region.class));
    }

    @Test
    void whenRegionIdExists_deleteRegion_DeletesTheRegion() {
        when(regionRepository.existsById(1)).thenReturn(true);
        regionService.deleteRegion(1);
        verify(regionRepository, times(1)).deleteById(1);
    }

    @Test
    void whenRegionIdDoesNotExist_deleteRegion_ThrowsIllegalArgumentException() {
        when(regionRepository.existsById(999)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> regionService.deleteRegion(999));
    }

    @Test
    void whenDeleteRegionFails_deleteRegion_ThrowsException() {
        when(regionRepository.existsById(1)).thenReturn(true);
        doThrow(new RuntimeException("Database error")).when(regionRepository).deleteById(1);

        assertThrows(RuntimeException.class, () -> regionService.deleteRegion(1));
    }

    @Test
    void whenRegionIdIsFound_updateRegion_UpdatesTheRegion() {
        RegionDTO updateDTO = new RegionDTO();
        updateDTO.setName("Updated Name");

        when(regionRepository.findById(1)).thenReturn(Optional.of(entity));
        when(regionRepository.existsById(1)).thenReturn(true);
        when(regionRepository.save(entity)).thenReturn(entity);
        when(regionMapper.mapToRegionDto(entity)).thenReturn(updateDTO);

        Optional<RegionDTO> result = regionService.updateRegion(1, updateDTO);

        assertTrue(result.isPresent());
        assertEquals("Updated Name", result.get().getName());
    }

    @Test
    void whenRegionIdIsNotFound_updateRegion_ReturnsEmptyOptional() {
        RegionDTO updateDTO = new RegionDTO();
        updateDTO.setName("Updated Name");

        when(regionRepository.findById(999)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> regionService.updateRegion(999, updateDTO));
    }

    @Test
    public void whenRegionNameIsInvalid_updateRegion_ThrowsIllegalArgumentException() {
        RegionDTO updateEmptyName = new RegionDTO();
        updateEmptyName.setName("");

        RegionDTO updateNullName = new RegionDTO();
        updateNullName.setName(null);

        when(regionRepository.existsById(1)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> regionService.updateRegion(1, updateEmptyName));
        assertThrows(IllegalArgumentException.class, () -> regionService.updateRegion(1, updateNullName));
    }

    @Test
    public void whenRegionNameAlreadyExists_updateRegion_ThrowsDataIntegrityViolationException() {
        when(regionRepository.findByName(dto.getName())).thenReturn(Optional.of(entity));

        assertThrows(IllegalArgumentException.class, () -> regionService.updateRegion(entity.getId(), dto));
    }
}
