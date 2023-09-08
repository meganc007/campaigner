package com.mcommings.campaigner.controllers.quests;

import com.mcommings.campaigner.models.quests.Reward;
import com.mcommings.campaigner.services.quests.RewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
