package com.mcommings.campaigner.modules.quests.controllers;

import com.mcommings.campaigner.modules.quests.dtos.RewardDTO;
import com.mcommings.campaigner.modules.quests.services.interfaces.IReward;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.mcommings.campaigner.enums.ErrorMessage.ID_NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/rewards")
public class RewardController {

    private final IReward rewardService;
    
    @GetMapping
    public List<RewardDTO> getRewards() {
        return rewardService.getRewards();
    }

    @GetMapping(path = "/{rewardId}")
    public RewardDTO getReward(@PathVariable("rewardId") int rewardId) {
        return rewardService.getReward(rewardId).orElseThrow(() -> new IllegalArgumentException(ID_NOT_FOUND.message));
    }

    @GetMapping("/campaign/{uuid}")
    public List<RewardDTO> getRewardsByCampaignUUID(@PathVariable("uuid") UUID uuid) {
        return rewardService.getRewardsByCampaignUUID(uuid);
    }

    @GetMapping(path = "/description/{keyword}")
    public List<RewardDTO> getRewardsWhereDescriptionContainsKeyword(@PathVariable("keyword") String keyword) {
        return rewardService.getRewardsWhereDescriptionContainsKeyword(keyword);
    }

    @GetMapping("/item/{itemId}")
    public List<RewardDTO> getRewardsByItemId(@PathVariable("itemId") int itemId) {
        return rewardService.getRewardsByItemId(itemId);
    }

    @GetMapping("/weapon/{weaponId}")
    public List<RewardDTO> getRewardsByWeaponId(@PathVariable("weaponId") int weaponId) {
        return rewardService.getRewardsByWeaponId(weaponId);
    }

    @PostMapping
    public void saveReward(@Valid @RequestBody RewardDTO reward) {
        rewardService.saveReward(reward);
    }

    @DeleteMapping(path = "{rewardId}")
    public void deleteReward(@PathVariable("rewardId") int rewardId) {
        rewardService.deleteReward(rewardId);
    }

    @PutMapping(path = "{rewardId}")
    public void updateReward(@PathVariable("rewardId") int rewardId, @RequestBody RewardDTO reward) {
        rewardService.updateReward(rewardId, reward);
    }
}
