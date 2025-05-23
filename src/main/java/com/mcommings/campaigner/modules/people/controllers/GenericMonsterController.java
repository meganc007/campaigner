package com.mcommings.campaigner.modules.people.controllers;

import com.mcommings.campaigner.modules.people.dtos.GenericMonsterDTO;
import com.mcommings.campaigner.modules.people.services.interfaces.IGenericMonster;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.mcommings.campaigner.enums.ErrorMessage.ID_NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/genericMonsters")
public class GenericMonsterController {

    private final IGenericMonster genericMonsterService;

    @GetMapping
    public List<GenericMonsterDTO> getGenericMonsters() {
        return genericMonsterService.getGenericMonsters();
    }

    @GetMapping(path = "/{genericMonsterId}")
    public GenericMonsterDTO getGenericMonster(@PathVariable("genericMonsterId") int genericMonsterId) {
        return genericMonsterService.getGenericMonster(genericMonsterId).orElseThrow(() -> new IllegalArgumentException(ID_NOT_FOUND.message));
    }

    @GetMapping(path = "/campaign/{uuid}")
    List<GenericMonsterDTO> getGenericMonstersByCampaignUUID(@PathVariable("uuid") UUID uuid) {
        return genericMonsterService.getGenericMonstersByCampaignUUID(uuid);
    }

    @GetMapping(path = "abilityScore/{abilityScoreId}")
    List<GenericMonsterDTO> getGenericMonstersByAbilityScore(@PathVariable("abilityScoreId") int abilityScoreId) {
        return genericMonsterService.getGenericMonstersByAbilityScore(abilityScoreId);
    }

    @PostMapping
    public void saveGenericMonster(@Valid @RequestBody GenericMonsterDTO genericMonster) {
        genericMonsterService.saveGenericMonster(genericMonster);
    }

    @DeleteMapping(path = "{genericMonsterId}")
    public void deleteGenericMonster(@PathVariable("genericMonsterId") int genericMonsterId) {
        genericMonsterService.deleteGenericMonster(genericMonsterId);
    }

    @PutMapping(path = "{genericMonsterId}")
    public void updateGenericMonster(@PathVariable("genericMonsterId") int genericMonsterId, @RequestBody GenericMonsterDTO genericMonster) {
        genericMonsterService.updateGenericMonster(genericMonsterId, genericMonster);
    }
}
