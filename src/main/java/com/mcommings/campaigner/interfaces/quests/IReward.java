package com.mcommings.campaigner.interfaces.quests;

import com.mcommings.campaigner.models.quests.Reward;

import java.util.List;

public interface IReward {

    List<Reward> getRewards();

    void saveReward(Reward reward);

    void deleteReward(int rewardId);

    void updateReward(int rewardId, Reward reward);
}
