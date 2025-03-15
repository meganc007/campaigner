package com.mcommings.campaigner.modules.quests.services.interfaces;

import com.mcommings.campaigner.modules.quests.entities.Reward;

import java.util.List;
import java.util.UUID;

public interface IReward {

    List<Reward> getRewards();

    List<Reward> getRewardsByCampaignUUID(UUID uuid);

    List<Reward> getRewardsByItemId(int itemId);

    List<Reward> getRewardsByWeaponId(int weaponId);

    void saveReward(Reward reward);

    void deleteReward(int rewardId);

    void updateReward(int rewardId, Reward reward);
}
