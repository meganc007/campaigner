package com.mcommings.campaigner.quests.controllers;

import com.mcommings.campaigner.quests.entities.Reward;
import com.mcommings.campaigner.quests.services.RewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/quests/rewards")
public class RewardController {

    private final RewardService rewardService;

    @Autowired
    public RewardController(RewardService rewardService) {
        this.rewardService = rewardService;
    }

    @GetMapping
    public List<Reward> getRewards() {
        return rewardService.getRewards();
    }

    @GetMapping("/campaign/{uuid}")
    public List<Reward> getRewardsByCampaignUUID(@PathVariable("uuid") UUID uuid) {
        return rewardService.getRewardsByCampaignUUID(uuid);
    }

    @GetMapping("/item/{itemId}")
    public List<Reward> getRewardsByItemId(@PathVariable("itemId") int itemId) {
        return rewardService.getRewardsByItemId(itemId);
    }

    @GetMapping("/weapon/{weaponId}")
    public List<Reward> getRewardsByWeaponId(@PathVariable("weaponId") int weaponId) {
        return rewardService.getRewardsByWeaponId(weaponId);
    }

    @PostMapping
    public void saveReward(@RequestBody Reward reward) {
        rewardService.saveReward(reward);
    }

    @DeleteMapping(path = "{rewardId}")
    public void deleteReward(@PathVariable("rewardId") int rewardId) {
        rewardService.deleteReward(rewardId);
    }

    @PutMapping(path = "{rewardId}")
    public void updateReward(@PathVariable("rewardId") int rewardId, @RequestBody Reward reward) {
        rewardService.updateReward(rewardId, reward);
    }
}
