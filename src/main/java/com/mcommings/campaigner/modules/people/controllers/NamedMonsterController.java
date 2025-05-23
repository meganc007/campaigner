package com.mcommings.campaigner.modules.people.controllers;

import com.mcommings.campaigner.modules.people.dtos.NamedMonsterDTO;
import com.mcommings.campaigner.modules.people.services.interfaces.INamedMonster;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.mcommings.campaigner.enums.ErrorMessage.ID_NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/namedMonsters")
public class NamedMonsterController {

    private final INamedMonster namedMonsterService;

    @GetMapping
    public List<NamedMonsterDTO> getNamedMonsters() {
        return namedMonsterService.getNamedMonsters();
    }

    @GetMapping(path = "/{namedMonsterId}")
    public NamedMonsterDTO getNamedMonster(@PathVariable("namedMonsterId") int namedMonsterId) {
        return namedMonsterService.getNamedMonster(namedMonsterId).orElseThrow(() -> new IllegalArgumentException(ID_NOT_FOUND.message));
    }

    @GetMapping(path = "/campaign/{uuid}")
    public List<NamedMonsterDTO> getNamedMonstersByCampaignUUID(@PathVariable("uuid") UUID uuid) {
        return namedMonsterService.getNamedMonstersByCampaignUUID(uuid);
    }

    @GetMapping(path = "/genericMonster/{genericMonsterId}")
    public List<NamedMonsterDTO> getNamedMonstersByGenericMonster(@PathVariable("genericMonsterId") int genericMonsterId) {
        return namedMonsterService.getNamedMonstersByGenericMonster(genericMonsterId);
    }

    @GetMapping(path = "/abilityScore/{abilityScoreId}")
    public List<NamedMonsterDTO> getNamedMonsterByAbilityScore(@PathVariable("abilityScoreId") int abilityScoreId) {
        return namedMonsterService.getNamedMonstersByAbilityScore(abilityScoreId);
    }

    @GetMapping(path = "/enemy/{isEnemy}")
    public List<NamedMonsterDTO> getNamedMonsterByEnemyStatus(@PathVariable("isEnemy") boolean isEnemy) {
        return namedMonsterService.getNamedMonstersByEnemyStatus(isEnemy);
    }

    @PostMapping
    public void saveNamedMonster(@Valid @RequestBody NamedMonsterDTO namedMonster) {
        namedMonsterService.saveNamedMonster(namedMonster);
    }

    @DeleteMapping(path = "{namedMonsterId}")
    public void deleteNamedMonster(@PathVariable("namedMonsterId") int namedMonsterId) {
        namedMonsterService.deleteNamedMonster(namedMonsterId);
    }

    @PutMapping(path = "{namedMonsterId}")
    public void updateNamedMonster(@PathVariable("namedMonsterId") int namedMonsterId, @RequestBody NamedMonsterDTO namedMonster) {
        namedMonsterService.updateNamedMonster(namedMonsterId, namedMonster);
    }
}
