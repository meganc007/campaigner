package com.mcommings.campaigner.services.quests;

import com.mcommings.campaigner.modules.quests.dtos.RewardDTO;
import com.mcommings.campaigner.modules.quests.entities.Reward;
import com.mcommings.campaigner.modules.quests.mappers.RewardMapper;
import com.mcommings.campaigner.modules.quests.repositories.IRewardRepository;
import com.mcommings.campaigner.modules.quests.services.RewardService;
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
public class RewardTest {

    @Mock
    private RewardMapper rewardMapper;

    @Mock
    private IRewardRepository rewardRepository;

    @InjectMocks
    private RewardService rewardService;

    private Reward entity;
    private RewardDTO dto;

    @BeforeEach
    void setUp() {
        Random random = new Random();
        entity = new Reward();
        entity.setId(1);
        entity.setDescription("This is a description.");
        entity.setNotes("This is a note.");
        entity.setFk_campaign_uuid(UUID.randomUUID());
        entity.setGold_value(random.nextInt(100) + 1);
        entity.setSilver_value(random.nextInt(100) + 1);
        entity.setCopper_value(random.nextInt(100) + 1);
        entity.setFk_item(random.nextInt(100) + 1);
        entity.setFk_weapon(random.nextInt(100) + 1);


        dto = new RewardDTO();
        dto.setId(entity.getId());
        dto.setDescription(entity.getDescription());
        dto.setNotes(entity.getNotes());
        dto.setFk_campaign_uuid(entity.getFk_campaign_uuid());
        dto.setGold_value(entity.getGold_value());
        dto.setSilver_value(entity.getSilver_value());
        dto.setCopper_value(entity.getCopper_value());
        dto.setFk_item(entity.getFk_item());
        dto.setFk_weapon(entity.getFk_weapon());

        when(rewardMapper.mapToRewardDto(entity)).thenReturn(dto);
        when(rewardMapper.mapFromRewardDto(dto)).thenReturn(entity);
    }

    @Test
    public void whenThereAreRewards_getRewards_ReturnsRewards() {
        when(rewardRepository.findAll()).thenReturn(List.of(entity));
        List<RewardDTO> result = rewardService.getRewards();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("This is a description.", result.get(0).getDescription());
    }

    @Test
    public void whenThereAreNoRewards_getRewards_ReturnsNothing() {
        when(rewardRepository.findAll()).thenReturn(Collections.emptyList());

        List<RewardDTO> result = rewardService.getRewards();

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected an empty list when there are no rewards.");
    }

    @Test
    public void whenThereIsAReward_getReward_ReturnsReward() {
        when(rewardRepository.findById(1)).thenReturn(Optional.of(entity));

        Optional<RewardDTO> result = rewardService.getReward(1);

        assertTrue(result.isPresent());
        assertEquals("This is a description.", result.get().getDescription());
    }

    @Test
    public void whenThereIsNotAReward_getReward_ReturnsReward() {
        when(rewardRepository.findById(999)).thenReturn(Optional.empty());

        Optional<RewardDTO> result = rewardService.getReward(999);

        assertTrue(result.isEmpty(), "Expected empty Optional when reward is not found.");
    }

    @Test
    public void whenCampaignUUIDIsValid_getRewardsByCampaignUUID_ReturnsRewards() {
        UUID campaignUUID = entity.getFk_campaign_uuid();
        when(rewardRepository.findByfk_campaign_uuid(campaignUUID)).thenReturn(List.of(entity));

        List<RewardDTO> result = rewardService.getRewardsByCampaignUUID(campaignUUID);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(campaignUUID, result.get(0).getFk_campaign_uuid());
    }

    @Test
    public void whenCampaignUUIDIsInvalid_getRewardsByCampaignUUID_ReturnsNothing() {
        UUID campaignUUID = UUID.randomUUID();
        when(rewardRepository.findByfk_campaign_uuid(campaignUUID)).thenReturn(Collections.emptyList());

        List<RewardDTO> result = rewardService.getRewardsByCampaignUUID(campaignUUID);

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected an empty list when no rewards match the campaign UUID.");
    }

    @Test
    public void whenRewardIsValid_saveReward_SavesTheReward() {
        when(rewardRepository.save(entity)).thenReturn(entity);

        rewardService.saveReward(dto);

        verify(rewardRepository, times(1)).save(entity);
    }

    @Test
    public void whenRewardDescriptionIsInvalid_saveReward_ThrowsIllegalArgumentException() {
        RewardDTO rewardWithEmptyDescription = new RewardDTO();
        rewardWithEmptyDescription.setId(1);
        rewardWithEmptyDescription.setDescription("");
        rewardWithEmptyDescription.setFk_campaign_uuid(UUID.randomUUID());

        RewardDTO rewardWithNullDescription = new RewardDTO();
        rewardWithNullDescription.setId(1);
        rewardWithNullDescription.setDescription(null);
        rewardWithNullDescription.setFk_campaign_uuid(UUID.randomUUID());

        assertThrows(IllegalArgumentException.class, () -> rewardService.saveReward(rewardWithEmptyDescription));
        assertThrows(IllegalArgumentException.class, () -> rewardService.saveReward(rewardWithNullDescription));
    }

    @Test
    public void whenRewardDescriptionAlreadyExists_saveReward_ThrowsDataIntegrityViolationException() {
        when(rewardRepository.rewardExists(rewardMapper.mapFromRewardDto(dto))).thenReturn(Optional.of(entity));
        assertThrows(DataIntegrityViolationException.class, () -> rewardService.saveReward(dto));
        verify(rewardRepository, times(1)).rewardExists(rewardMapper.mapFromRewardDto(dto));
        verify(rewardRepository, never()).save(any(Reward.class));
    }

    @Test
    public void whenRewardIdExists_deleteReward_DeletesTheReward() {
        when(rewardRepository.existsById(1)).thenReturn(true);
        rewardService.deleteReward(1);
        verify(rewardRepository, times(1)).deleteById(1);
    }

    @Test
    public void whenRewardIdDoesNotExist_deleteReward_ThrowsIllegalArgumentException() {
        when(rewardRepository.existsById(999)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> rewardService.deleteReward(999));
    }

    @Test
    public void whenDeleteRewardFails_deleteReward_ThrowsException() {
        when(rewardRepository.existsById(1)).thenReturn(true);
        doThrow(new RuntimeException("Database error")).when(rewardRepository).deleteById(1);

        assertThrows(RuntimeException.class, () -> rewardService.deleteReward(1));
    }

    @Test
    public void whenRewardIdIsFound_updateReward_UpdatesTheReward() {
        RewardDTO updateDTO = new RewardDTO();
        updateDTO.setDescription("Updated description");

        when(rewardRepository.findById(1)).thenReturn(Optional.of(entity));
        when(rewardRepository.existsById(1)).thenReturn(true);
        when(rewardRepository.save(entity)).thenReturn(entity);
        when(rewardMapper.mapToRewardDto(entity)).thenReturn(updateDTO);

        Optional<RewardDTO> result = rewardService.updateReward(1, updateDTO);

        assertTrue(result.isPresent());
        assertEquals("Updated description", result.get().getDescription());
    }

    @Test
    public void whenRewardIdIsNotFound_updateReward_ReturnsEmptyOptional() {
        RewardDTO updateDTO = new RewardDTO();
        updateDTO.setDescription("Updated");

        when(rewardRepository.findById(999)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> rewardService.updateReward(999, updateDTO));
    }

    @Test
    public void whenRewardDescriptionIsInvalid_updateReward_ThrowsIllegalArgumentException() {
        RewardDTO updateEmptyDescription = new RewardDTO();
        updateEmptyDescription.setDescription("");

        RewardDTO updateNullDescription = new RewardDTO();
        updateNullDescription.setDescription(null);

        when(rewardRepository.existsById(1)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> rewardService.updateReward(1, updateEmptyDescription));
        assertThrows(IllegalArgumentException.class, () -> rewardService.updateReward(1, updateNullDescription));
    }

    @Test
    public void whenRewardNameAlreadyExists_updateReward_ThrowsDataIntegrityViolationException() {
        when(rewardRepository.rewardExists(rewardMapper.mapFromRewardDto(dto))).thenReturn(Optional.of(entity));

        assertThrows(IllegalArgumentException.class, () -> rewardService.updateReward(entity.getId(), dto));
    }
}
