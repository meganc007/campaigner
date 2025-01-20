package com.mcommings.campaigner.services;

import com.mcommings.campaigner.models.Campaign;
import com.mcommings.campaigner.repositories.ICampaignRepository;
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
public class CampaignTest {
    
    @Mock
    private ICampaignRepository campaignRepository;
    
    @InjectMocks
    private CampaignService campaignService;
    
    @Test
    public void whenThereAreCampaigns_getCampaigns_ReturnsCampaigns() {
        List<Campaign> campaigns = new ArrayList<>();
        campaigns.add(new Campaign("Campaign 1", "Description 1"));
        campaigns.add(new Campaign("Campaign 2", "Description 2"));

        when(campaignRepository.findAll()).thenReturn(campaigns);

        List<Campaign> result = campaignService.getCampaigns();

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(campaigns, result);
    }

    @Test
    public void whenThereAreNoCampaigns_getCampaigns_ReturnsNothing() {
        List<Campaign> campaigns = new ArrayList<>();
        when(campaignRepository.findAll()).thenReturn(campaigns);

        List<Campaign> result = campaignService.getCampaigns();

        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(campaigns, result);
    }

    @Test
    public void whenCampaignUUIDIsValid_getCampaign_ReturnsCampaign() {
        Campaign campaign = new Campaign("City 1", "Description 1");
        UUID uuid = campaign.getUuid();

        when(campaignRepository.findByUuid(uuid)).thenReturn(Optional.of(campaign));

        Campaign result = campaignService.getCampaign(uuid);

        Assertions.assertNotNull(campaign);
        Assertions.assertEquals(campaign, result);
    }

    @Test
    public void whenCampaignUUIDIsInvalid_getCampaign_ThrowsIllegalArgumentException() {
        UUID uuid = UUID.randomUUID();
        assertThrows(IllegalArgumentException.class, () -> campaignService.getCampaign(uuid));
    }

    @Test
    public void whenCampaignIsValid_saveCampaign_SavesTheCampaign() {
        Campaign campaign = new Campaign("Campaign 1", "Description 1");
        when(campaignRepository.saveAndFlush(campaign)).thenReturn(campaign);

        assertDoesNotThrow(() -> campaignService.saveCampaign(campaign));
        verify(campaignRepository, times(1)).saveAndFlush(campaign);
    }

    @Test
    public void whenCampaignNameIsInvalid_saveCampaign_ThrowsIllegalArgumentException() {
        Campaign campaignWithEmptyName = new Campaign("", "Description 1");
        Campaign campaignWithNullName = new Campaign(null, "Description 2");

        assertThrows(IllegalArgumentException.class, () -> campaignService.saveCampaign(campaignWithEmptyName));
        assertThrows(IllegalArgumentException.class, () -> campaignService.saveCampaign(campaignWithNullName));
    }

    @Test
    public void whenCampaignNameAlreadyExists_saveCampaign_ThrowsDataIntegrityViolationException() {
        Campaign campaign = new Campaign("Campaign 1", "Description 1");
        Campaign campaignWithDuplicatedName = new Campaign("Campaign 1", "Description 2");
        when(campaignRepository.saveAndFlush(campaign)).thenReturn(campaign);
        when(campaignRepository.saveAndFlush(campaignWithDuplicatedName)).thenThrow(DataIntegrityViolationException.class);

        assertDoesNotThrow(() -> campaignService.saveCampaign(campaign));
        assertThrows(DataIntegrityViolationException.class, () -> campaignService.saveCampaign(campaignWithDuplicatedName));
    }
    
    @Test
    public void whenCampaignIdExists_deleteCampaign_DeletesTheCampaign() {
        UUID uuid = UUID.randomUUID();
        Campaign campaign = new Campaign("Test", "test");
        campaign.setUuid(uuid);

        when(campaignRepository.existsByUuid(uuid)).thenReturn(true);
        when(campaignRepository.findByUuid(uuid)).thenReturn(Optional.of(campaign));

        assertDoesNotThrow(() -> campaignService.deleteCampaign(uuid));
        verify(campaignRepository, times(1)).deleteByUuid(uuid);
    }

    @Test
    public void whenCampaignIdDoesNotExist_deleteCampaign_ThrowsIllegalArgumentException() {
        UUID uuid = UUID.randomUUID();
        when(campaignRepository.existsByUuid(uuid)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> campaignService.deleteCampaign(uuid));
    }
    
    @Test
    public void whenCampaignIdIsFound_updateCampaign_UpdatesTheCampaign() {
        UUID uuid = UUID.randomUUID();
        Campaign campaign = new Campaign("Old Campaign Name", "Old Description");
        campaign.setUuid(uuid);
        Campaign campaignToUpdate = new Campaign("Updated Campaign Name", "Updated Description");

        when(campaignRepository.existsByUuid(uuid)).thenReturn(true);
        when(campaignRepository.findByUuid(uuid)).thenReturn(Optional.of(campaign));

        campaignService.updateCampaign(uuid, campaignToUpdate);

        verify(campaignRepository, times(2)).findByUuid(uuid);

        Campaign result = campaignRepository.findByUuid(uuid).get();
        Assertions.assertEquals(campaignToUpdate.getName(), result.getName());
        Assertions.assertEquals(campaignToUpdate.getDescription(), result.getDescription());
    }

    @Test
    public void whenCampaignIdIsNotFound_updateCampaign_ThrowsIllegalArgumentException() {
        UUID uuid = UUID.randomUUID();
        Campaign campaign = new Campaign("Old Campaign Name", "Old Description");
        campaign.setUuid(uuid);

        when(campaignRepository.existsByUuid(uuid)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> campaignService.updateCampaign(uuid, campaign));
    }

    @Test
    public void whenSomeCampaignFieldsChanged_updateCampaign_OnlyUpdatesChangedFields() {
        UUID uuid = UUID.randomUUID();
        Campaign campaign = new Campaign("Old Campaign Name", "Old Description");
        campaign.setUuid(uuid);
        Campaign campaignToUpdate = new Campaign();
        campaignToUpdate.setDescription("New campaign description");

        when(campaignRepository.existsByUuid(uuid)).thenReturn(true);
        when(campaignRepository.findByUuid(uuid)).thenReturn(Optional.of(campaign));

        campaignService.updateCampaign(uuid, campaignToUpdate);

        verify(campaignRepository, times(2)).findByUuid(uuid);

        Campaign result = campaignRepository.findByUuid(uuid).get();
        Assertions.assertEquals("Old Campaign Name", result.getName());
        Assertions.assertEquals(campaignToUpdate.getDescription(), result.getDescription());
    }
}
