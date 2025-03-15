package com.mcommings.campaigner.modules.items.controllers;

import com.mcommings.campaigner.modules.items.entities.DiceType;
import com.mcommings.campaigner.modules.items.services.DiceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/items/dice-types")
public class DiceTypeController {

    private final DiceTypeService diceTypeService;

    @Autowired
    public DiceTypeController(DiceTypeService diceTypeService) {
        this.diceTypeService = diceTypeService;
    }

    @GetMapping
    public List<DiceType> DiceType() {
        return diceTypeService.getDiceTypes();
    }

    @PostMapping
    public void saveDiceType(@RequestBody DiceType diceType) {
        diceTypeService.saveDiceType(diceType);
    }

    @DeleteMapping(path = "{diceTypeId}")
    public void deleteDiceType(@PathVariable("diceTypeId") int diceTypeId) {
        diceTypeService.deleteDiceType(diceTypeId);
    }

    @PutMapping(path = "{diceTypeId}")
    public void updateDiceType(@PathVariable("diceTypeId") int diceTypeId, @RequestBody DiceType diceType) {
        diceTypeService.updateDiceType(diceTypeId, diceType);
    }
}
