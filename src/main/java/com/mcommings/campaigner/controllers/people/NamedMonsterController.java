package com.mcommings.campaigner.controllers.people;

import com.mcommings.campaigner.models.people.NamedMonster;
import com.mcommings.campaigner.services.people.NamedMonsterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
