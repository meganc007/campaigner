package com.mcommings.campaigner.modules.quests.services.interfaces;

import com.mcommings.campaigner.modules.quests.dtos.RewardDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IReward {

    List<RewardDTO> getRewards();

    Optional<RewardDTO> getReward(int rewardId);

    List<RewardDTO> getRewardsByCampaignUUID(UUID uuid);

    List<RewardDTO> getRewardsWhereDescriptionContainsKeyword(String keyword);

    List<RewardDTO> getRewardsByItemId(int itemId);

    List<RewardDTO> getRewardsByWeaponId(int weaponId);

    void saveReward(RewardDTO reward);

    void deleteReward(int rewardId);

    Optional<RewardDTO> updateReward(int rewardId, RewardDTO reward);
}
