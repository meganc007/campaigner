package com.mcommings.campaigner.people.controllers;

import com.mcommings.campaigner.people.entities.NamedMonster;
import com.mcommings.campaigner.people.services.NamedMonsterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/namedMonsters/named-monsters")
public class NamedMonsterController {

    private final NamedMonsterService namedMonsterService;

    @Autowired
    public NamedMonsterController(NamedMonsterService namedMonsterService) {
        this.namedMonsterService = namedMonsterService;
    }

    @GetMapping
    public List<NamedMonster> getNamedMonsters() {
        return namedMonsterService.getNamedMonsters();
    }

    @GetMapping(path = "/campaign/{uuid}")
    public List<NamedMonster> getNamedMonstersByCampaignUUID(@PathVariable("uuid") UUID uuid) {
        return namedMonsterService.getNamedMonstersByCampaignUUID(uuid);
    }

    @GetMapping(path = "/genericMonster/{id}")
    public List<NamedMonster> getNamedMonstersByGenericMonster(int id) {
        return namedMonsterService.getNamedMonstersByGenericMonster(id);
    }

    @PostMapping
    public void saveNamedMonster(@RequestBody NamedMonster namedMonster) {
        namedMonsterService.saveNamedMonster(namedMonster);
    }

    @DeleteMapping(path = "{namedMonsterId}")
    public void deleteNamedMonster(@PathVariable("namedMonsterId") int namedMonsterId) {
        namedMonsterService.deleteNamedMonster(namedMonsterId);
    }

    @PutMapping(path = "{namedMonsterId}")
    public void updateNamedMonster(@PathVariable("namedMonsterId") int namedMonsterId, @RequestBody NamedMonster namedMonster) {
        namedMonsterService.updateNamedMonster(namedMonsterId, namedMonster);
    }
}
