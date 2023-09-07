package com.mcommings.campaigner.controllers.people;

import com.mcommings.campaigner.models.people.GenericMonster;
import com.mcommings.campaigner.services.people.GenericMonsterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/people/generic-monsters")
public class GenericMonsterController {

    private final GenericMonsterService genericMonsterService;

    @Autowired
    public GenericMonsterController(GenericMonsterService genericMonsterService) {
        this.genericMonsterService = genericMonsterService;
    }

    @GetMapping
    public List<GenericMonster> GenericMonster() {
        return genericMonsterService.getGenericMonsters();
    }

    @PostMapping
    public void saveGenericMonster(@RequestBody GenericMonster genericMonster) {
        genericMonsterService.saveGenericMonster(genericMonster);
    }

    @DeleteMapping(path = "{genericMonsterId}")
    public void deleteGenericMonster(@PathVariable("genericMonsterId") int genericMonsterId) {
        genericMonsterService.deleteGenericMonster(genericMonsterId);
    }

    @PutMapping(path = "{genericMonsterId}")
    public void updateGenericMonster(@PathVariable("genericMonsterId") int genericMonsterId, @RequestBody GenericMonster genericMonster) {
        genericMonsterService.updateGenericMonster(genericMonsterId, genericMonster);
    }
}
