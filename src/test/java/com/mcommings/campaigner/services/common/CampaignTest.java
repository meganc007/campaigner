package com.mcommings.campaigner.services.common;

import com.mcommings.campaigner.modules.common.dtos.CampaignDTO;
import com.mcommings.campaigner.modules.common.entities.Campaign;
import com.mcommings.campaigner.modules.common.mappers.CampaignMapper;
import com.mcommings.campaigner.modules.common.repositories.ICampaignRepository;
import com.mcommings.campaigner.modules.common.services.CampaignService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CampaignTest {

    @Mock
    private CampaignMapper campaignMapper;
    
    @Mock
    private ICampaignRepository campaignRepository;
    
    @InjectMocks
    private CampaignService campaignService;

    private Campaign entity;
    private CampaignDTO dto;

    @BeforeEach
    void setUp() {
        entity = new Campaign();
        entity.setUuid(UUID.randomUUID());
        entity.setName("Test Campaign");
        entity.setDescription("A fictional land.");

        dto = new CampaignDTO();
        dto.setUuid(entity.getUuid());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());

        // Mocking the mapper behavior
        when(campaignMapper.mapToCampaignDto(entity)).thenReturn(dto);
        when(campaignMapper.mapFromCampaignDto(dto)).thenReturn(entity);
    }
    
    @Test
    public void whenThereAreCampaigns_getCampaigns_ReturnsCampaigns() {
        when(campaignRepository.findAll()).thenReturn(List.of(entity));
        List<CampaignDTO> result = campaignService.getCampaigns();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Campaign", result.get(0).getName());
    }

    @Test
    public void whenThereAreNoCampaigns_getCampaigns_ReturnsEmptyList() {
        when(campaignRepository.findAll()).thenReturn(Collections.emptyList());

        List<CampaignDTO> result = campaignService.getCampaigns();

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected an empty list when there are no campaigns.");
    }

    @Test
    void getCampaign_ReturnsCampaignById() {
        UUID uuid = UUID.randomUUID();
        when(campaignRepository.findByUuid(uuid)).thenReturn(Optional.of(entity));

        Optional<CampaignDTO> result = campaignService.getCampaign(uuid);

        assertTrue(result.isPresent());
        assertEquals("Test Campaign", result.get().getName());
    }

    @Test
    void whenThereIsNotACampaign_getCampaign_ReturnsNothing() {
        UUID uuid = UUID.randomUUID();
        when(campaignRepository.findByUuid(uuid)).thenReturn(Optional.empty());

        Optional<CampaignDTO> result = campaignService.getCampaign(uuid);

        assertTrue(result.isEmpty(), "Expected empty Optional when campaign is not found.");
    }

    @Test
    void whenCampaignIsValid_saveCampaign_SavesTheCampaign() {
        when(campaignRepository.save(entity)).thenReturn(entity);

        campaignService.saveCampaign(dto);

        verify(campaignRepository, times(1)).save(entity);
    }

    @Test
    public void whenCampaignNameIsInvalid_saveCampaign_ThrowsIllegalArgumentException() {
        UUID uuid = UUID.randomUUID();
        CampaignDTO campaignWithEmptyName = new CampaignDTO();
        campaignWithEmptyName.setUuid(uuid);
        campaignWithEmptyName.setName("");
        campaignWithEmptyName.setDescription("A fictional campaign.");

        CampaignDTO campaignWithNullName = new CampaignDTO();
        campaignWithNullName.setUuid(uuid);
        campaignWithNullName.setName(null);
        campaignWithNullName.setDescription("A fictional campaign.");

        assertThrows(IllegalArgumentException.class, () -> campaignService.saveCampaign(campaignWithEmptyName));
        assertThrows(IllegalArgumentException.class, () -> campaignService.saveCampaign(campaignWithNullName));
    }

    @Test
    public void whenCampaignNameAlreadyExists_saveCampaign_ThrowsDataIntegrityViolationException() {
        when(campaignRepository.findByName(dto.getName())).thenReturn(Optional.of(entity));
        assertThrows(DataIntegrityViolationException.class, () -> campaignService.saveCampaign(dto));
        verify(campaignRepository, times(1)).findByName(dto.getName());
        verify(campaignRepository, never()).save(any(Campaign.class));
    }

//    @Test
//    void whenCampaignUuidExists_deleteCampaign_DeletesTheCampaign() {
//        UUID uuid = UUID.randomUUID();
//        when(campaignRepository.existsByUuid(uuid)).thenReturn(true);
//
//        campaignService.deleteCampaign(uuid);
//
//        verify(campaignRepository).deleteByUuid(uuid);
//    }

    @Test
    void whenCampaignUuidExists_deleteCampaign_DeletesTheCampaign() {
        UUID uuid = UUID.randomUUID();
        Campaign campaign = new Campaign();
        campaign.setUuid(uuid);

        when(campaignRepository.findByUuid(uuid)).thenReturn(Optional.of(campaign));

        campaignService.deleteCampaign(uuid);

        verify(campaignRepository).findByUuid(uuid);
        verify(campaignRepository).deleteByUuid(uuid);
    }

    @Test
    void whenCampaignUuidDoesNotExist_deleteCampaign_ThrowsIllegalArgumentException() {
        UUID uuid = UUID.randomUUID();
        when(campaignRepository.existsByUuid(uuid)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> campaignService.deleteCampaign(uuid));
    }

    @Test
    void whenDeleteCampaignFails_deleteCampaign_ThrowsException() {
        UUID uuid = UUID.randomUUID();
        when(campaignRepository.existsByUuid(uuid)).thenReturn(true);
        doThrow(new RuntimeException("Database error")).when(campaignRepository).deleteByUuid(uuid);

        assertThrows(RuntimeException.class, () -> campaignService.deleteCampaign(uuid));
    }

    @Test
    void whenCampaignUuidIsFound_updateCampaign_UpdatesTheCampaign() {
        UUID uuid = UUID.randomUUID();
        CampaignDTO updateDTO = new CampaignDTO();
        updateDTO.setName("Updated Name");

        when(campaignRepository.findByUuid(uuid)).thenReturn(Optional.of(entity));
        when(campaignRepository.existsByUuid(uuid)).thenReturn(true);
        when(campaignRepository.findByName("Updated Name")).thenReturn(Optional.empty());
        when(campaignRepository.save(entity)).thenReturn(entity);
        when(campaignMapper.mapToCampaignDto(entity)).thenReturn(updateDTO);

        Optional<CampaignDTO> result = campaignService.updateCampaign(uuid, updateDTO);

        assertTrue(result.isPresent());
        assertEquals("Updated Name", result.get().getName());
    }

    @Test
    void whenCampaignUuidIsNotFound_updateCampaign_ReturnsEmptyOptional() {
        UUID uuid = UUID.randomUUID();
        CampaignDTO updateDTO = new CampaignDTO();
        updateDTO.setName("Updated Name");

        when(campaignRepository.findByUuid(uuid)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> campaignService.updateCampaign(uuid, updateDTO));
    }

    @Test
    public void whenCampaignNameIsInvalid_updateCampaign_ThrowsIllegalArgumentException() {
        UUID uuid = UUID.randomUUID();
        CampaignDTO updateEmptyName = new CampaignDTO();
        updateEmptyName.setName("");

        CampaignDTO updateNullName = new CampaignDTO();
        updateNullName.setName(null);

        when(campaignRepository.existsByUuid(uuid)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> campaignService.updateCampaign(uuid, updateEmptyName));
        assertThrows(IllegalArgumentException.class, () -> campaignService.updateCampaign(uuid, updateNullName));
    }

    @Test
    public void whenCampaignNameAlreadyExists_updateCampaign_ThrowsDataIntegrityViolationException() {
        UUID uuid = UUID.randomUUID();
        UUID otherUuid = UUID.randomUUID();

        Campaign entity = new Campaign();
        entity.setUuid(otherUuid);

        CampaignDTO dto = new CampaignDTO();
        dto.setUuid(uuid);
        dto.setName("Conflicting Name");

        when(campaignRepository.existsByUuid(uuid)).thenReturn(true);
        when(campaignRepository.findByUuid(uuid)).thenReturn(Optional.of(entity));
        when(campaignRepository.findByName(dto.getName()))
                .thenReturn(Optional.of(entity));
    }
}
