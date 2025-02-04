package com.mcommings.campaigner.services.quests;

import com.mcommings.campaigner.entities.quests.Reward;
import com.mcommings.campaigner.repositories.items.IItemRepository;
import com.mcommings.campaigner.repositories.items.IWeaponRepository;
import com.mcommings.campaigner.repositories.quests.IRewardRepository;
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
public class RewardTest {

    @Mock
    private IRewardRepository rewardRepository;
    @Mock
    private IItemRepository itemRepository;
    @Mock
    private IWeaponRepository weaponRepository;

    @InjectMocks
    private RewardService rewardService;

    @Test
    public void whenThereAreRewards_getRewards_ReturnsRewards() {
        List<Reward> rewards = new ArrayList<>();
        UUID campaign = UUID.randomUUID();
        rewards.add(new Reward(1, "Description", "Notes", campaign));
        rewards.add(new Reward(2, "Description", "Notes", 200, 100, 50, campaign));
        rewards.add(new Reward(3, "Description", "Notes", 200, 100, 50, 1, 2, campaign));
        when(rewardRepository.findAll()).thenReturn(rewards);

        List<Reward> result = rewardService.getRewards();

        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals(rewards, result);
    }

    @Test
    public void whenThereAreNoRewards_getRewards_ReturnsNothing() {
        List<Reward> rewards = new ArrayList<>();
        when(rewardRepository.findAll()).thenReturn(rewards);

        List<Reward> result = rewardService.getRewards();

        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(rewards, result);
    }

    @Test
    public void whenCampaignUUIDIsValid_getRewardsByCampaignUUID_ReturnsRewards() {
        List<Reward> rewards = new ArrayList<>();
        UUID campaign = UUID.randomUUID();
        rewards.add(new Reward(1, "Description", "Notes", campaign));
        rewards.add(new Reward(2, "Description", "Notes", 200, 100, 50, campaign));
        rewards.add(new Reward(3, "Description", "Notes", 200, 100, 50, 1, 2, campaign));
        when(rewardRepository.findByfk_campaign_uuid(campaign)).thenReturn(rewards);

        List<Reward> results = rewardService.getRewardsByCampaignUUID(campaign);

        Assertions.assertEquals(3, results.size());
        Assertions.assertEquals(rewards, results);
    }

    @Test
    public void whenCampaignUUIDIsInvalid_getRewardsByCampaignUUID_ReturnsNothing() {
        UUID campaign = UUID.randomUUID();
        List<Reward> rewards = new ArrayList<>();
        when(rewardRepository.findByfk_campaign_uuid(campaign)).thenReturn(rewards);

        List<Reward> result = rewardService.getRewardsByCampaignUUID(campaign);

        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(rewards, result);
    }

    @Test
    public void whenRewardIsValid_saveReward_SavesTheReward() {
        Reward reward = new Reward(3, "Description", "Notes", 200, 100, 50, UUID.randomUUID());
        reward.setFk_item(1);
        reward.setFk_weapon(1);

        when(itemRepository.existsById(1)).thenReturn(true);
        when(weaponRepository.existsById(1)).thenReturn(true);
        when(rewardRepository.saveAndFlush(reward)).thenReturn(reward);

        assertDoesNotThrow(() -> rewardService.saveReward(reward));

        verify(rewardRepository, times(1)).saveAndFlush(reward);
    }

    @Test
    public void whenRewardHasOneFk_saveReward_SavesTheReward() {
        Reward reward = new Reward(3, "Description", "Notes", 200, 100, 50, UUID.randomUUID());
        reward.setFk_item(1);

        when(itemRepository.existsById(1)).thenReturn(true);
        when(rewardRepository.saveAndFlush(reward)).thenReturn(reward);

        assertDoesNotThrow(() -> rewardService.saveReward(reward));
        verify(rewardRepository, times(1)).saveAndFlush(reward);
    }

    @Test
    public void whenRewardAlreadyExists_saveReward_ThrowsDataIntegrityViolationException() {
        Reward reward = new Reward(1, "Description", "Notes", 200, 100, 50, UUID.randomUUID());
        reward.setFk_item(1);
        reward.setFk_weapon(1);
        Reward copy = new Reward(2, "Description", "Notes", 200, 100, 50, UUID.randomUUID());
        copy.setFk_item(1);
        copy.setFk_weapon(1);

        when(itemRepository.existsById(1)).thenReturn(true);
        when(weaponRepository.existsById(1)).thenReturn(true);

        when(rewardRepository.saveAndFlush(reward)).thenReturn(reward);
        when(rewardRepository.saveAndFlush(copy)).thenThrow(DataIntegrityViolationException.class);

        assertDoesNotThrow(() -> rewardService.saveReward(reward));
        assertThrows(DataIntegrityViolationException.class, () -> rewardService.saveReward(copy));
    }

    @Test
    public void whenRewardIdExists_deleteReward_DeletesTheReward() {
        int rewardId = 1;
        when(rewardRepository.existsById(rewardId)).thenReturn(true);
        assertDoesNotThrow(() -> rewardService.deleteReward(rewardId));
        verify(rewardRepository, times(1)).deleteById(rewardId);
    }

    @Test
    public void whenRewardIdDoesNotExist_deleteReward_ThrowsIllegalArgumentException() {
        int rewardId = 9000;
        when(rewardRepository.existsById(rewardId)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> rewardService.deleteReward(rewardId));
    }

    //TODO: test delete when Reward is a fk

    @Test
    public void whenRewardIdWithValidFKIsFound_updateReward_UpdatesTheReward() {
        int rewardId = 1;
        UUID campaign = UUID.randomUUID();

        Reward reward = new Reward(rewardId, "Description", "Notes", 200, 100, 50, 1, 1, campaign);
        Reward update = new Reward(rewardId, "Description", "Notes", 200, 100, 50, 2, 2, campaign);

        when(rewardRepository.existsById(rewardId)).thenReturn(true);
        when(rewardRepository.findById(rewardId)).thenReturn(Optional.of(reward));
        when(itemRepository.existsById(1)).thenReturn(true);
        when(weaponRepository.existsById(1)).thenReturn(true);
        when(itemRepository.existsById(2)).thenReturn(true);
        when(weaponRepository.existsById(2)).thenReturn(true);

        rewardService.updateReward(rewardId, update);

        verify(rewardRepository).findById(rewardId);

        Reward result = rewardRepository.findById(rewardId).get();
        Assertions.assertEquals(update.getId(), result.getId());
        Assertions.assertEquals(update.getDescription(), result.getDescription());
        Assertions.assertEquals(update.getNotes(), result.getNotes());
        Assertions.assertEquals(update.getGold_value(), result.getGold_value());
        Assertions.assertEquals(update.getSilver_value(), result.getSilver_value());
        Assertions.assertEquals(update.getCopper_value(), result.getCopper_value());
        Assertions.assertEquals(update.getFk_item(), result.getFk_item());
        Assertions.assertEquals(update.getFk_weapon(), result.getFk_weapon());
    }

    @Test
    public void whenRewardIdWithInvalidFKIsFound_updateReward_ThrowsDataIntegrityViolationException() {
        int rewardId = 1;
        UUID campaign = UUID.randomUUID();

        Reward reward = new Reward(rewardId, "Description", "Notes", 200, 100, 50, 1, 1, campaign);
        Reward update = new Reward(rewardId, "Description", "Notes", 200, 100, 50, 2, 2, campaign);

        when(rewardRepository.existsById(rewardId)).thenReturn(true);
        when(rewardRepository.findById(rewardId)).thenReturn(Optional.of(reward));
        when(itemRepository.existsById(1)).thenReturn(false);
        when(weaponRepository.existsById(1)).thenReturn(true);
        when(itemRepository.existsById(2)).thenReturn(false);
        when(weaponRepository.existsById(2)).thenReturn(true);

        assertThrows(DataIntegrityViolationException.class, () -> rewardService.updateReward(rewardId, update));
    }

    @Test
    public void whenRewardIdIsNotFound_updateReward_ThrowsIllegalArgumentException() {
        int rewardId = 1;
        Reward reward = new Reward(rewardId, "Description", "Notes", UUID.randomUUID());

        when(rewardRepository.existsById(rewardId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> rewardService.updateReward(rewardId, reward));
    }

    @Test
    public void whenSomeRewardFieldsChanged_updateReward_OnlyUpdatesChangedFields() {
        int rewardId = 1;
        Reward reward = new Reward(rewardId, "Description", "Notes",
                200, 100, 50, 1, 1, UUID.randomUUID());

        String newDescription = "New Reward description";
        int newGoldValue = 3000;
        int newWeapon = 4;

        Reward rewardToUpdate = new Reward();
        rewardToUpdate.setDescription(newDescription);
        rewardToUpdate.setGold_value(newGoldValue);
        rewardToUpdate.setFk_weapon(newWeapon);

        when(rewardRepository.existsById(rewardId)).thenReturn(true);
        when(itemRepository.existsById(1)).thenReturn(true);
        when(weaponRepository.existsById(newWeapon)).thenReturn(true);
        when(rewardRepository.findById(rewardId)).thenReturn(Optional.of(reward));

        rewardService.updateReward(rewardId, rewardToUpdate);

        verify(rewardRepository).findById(rewardId);

        Reward result = rewardRepository.findById(rewardId).get();
        Assertions.assertEquals(newDescription, result.getDescription());
        Assertions.assertEquals(reward.getNotes(), result.getNotes());
        Assertions.assertEquals(newGoldValue, result.getGold_value());
        Assertions.assertEquals(reward.getSilver_value(), result.getSilver_value());
        Assertions.assertEquals(reward.getCopper_value(), result.getCopper_value());
        Assertions.assertEquals(reward.getFk_item(), result.getFk_item());
        Assertions.assertEquals(newWeapon, result.getFk_weapon());
    }
}
