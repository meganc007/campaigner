package com.mcommings.campaigner.controllers.people;

import com.mcommings.campaigner.models.people.AbilityScore;
import com.mcommings.campaigner.services.people.AbilityScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/people/ability-scores")
public class AbilityScoreController {

    private final AbilityScoreService abilityScoreService;

    @Autowired
    public AbilityScoreController(AbilityScoreService abilityScoreService) {
        this.abilityScoreService = abilityScoreService;
    }

    @GetMapping
    public List<AbilityScore> getAbilityScores() {
        return abilityScoreService.getAbilityScores();
    }

    @PostMapping
    public void saveAbilityScore(@RequestBody AbilityScore abilityScore) {
        abilityScoreService.saveAbilityScore(abilityScore);
    }

    @DeleteMapping(path = "{id}")
    public void deleteAbilityScore(@PathVariable("id") int id) {
        abilityScoreService.deleteAbilityScore(id);
    }

    @PutMapping(path = "{id}")
    public void updateAbilityScore(@PathVariable("id") int id, @RequestBody AbilityScore abilityScore) {
        abilityScoreService.updateAbilityScore(id, abilityScore);
    }

}
