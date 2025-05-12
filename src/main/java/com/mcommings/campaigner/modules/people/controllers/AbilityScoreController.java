package com.mcommings.campaigner.modules.people.controllers;

import com.mcommings.campaigner.modules.people.dtos.AbilityScoreDTO;
import com.mcommings.campaigner.modules.people.services.interfaces.IAbilityScore;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.mcommings.campaigner.enums.ErrorMessage.ID_NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/ability-scores")
public class AbilityScoreController {

    private final IAbilityScore abilityScoreService;

    @GetMapping
    public List<AbilityScoreDTO> getAbilityScores() {
        return abilityScoreService.getAbilityScores();
    }

    @GetMapping(path = "/{id}")
    public AbilityScoreDTO getAbilityScore(@PathVariable("id") int id) {
        return abilityScoreService.getAbilityScore(id).orElseThrow(() -> new IllegalArgumentException(ID_NOT_FOUND.message));
    }

    @GetMapping(path = "/campaign/{uuid}")
    public List<AbilityScoreDTO> getAbilityScoresByCampaignUUID(@PathVariable("uuid") UUID uuid) {
        return abilityScoreService.getAbilityScoresByCampaignUUID(uuid);
    }

    @PostMapping
    public void saveAbilityScore(@Valid @RequestBody AbilityScoreDTO abilityScore) {
        abilityScoreService.saveAbilityScore(abilityScore);
    }

    @DeleteMapping(path = "{id}")
    public void deleteAbilityScore(@PathVariable("id") int id) {
        abilityScoreService.deleteAbilityScore(id);
    }

    @PutMapping(path = "{id}")
    public void updateAbilityScore(@PathVariable("id") int id, @RequestBody AbilityScoreDTO abilityScore) {
        abilityScoreService.updateAbilityScore(id, abilityScore);
    }

}
