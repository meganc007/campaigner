package com.mcommings.campaigner.modules.items.controllers;

import com.mcommings.campaigner.modules.items.dtos.dice_types.CreateDiceTypeDTO;
import com.mcommings.campaigner.modules.items.dtos.dice_types.UpdateDiceTypeDTO;
import com.mcommings.campaigner.modules.items.dtos.dice_types.ViewDiceTypeDTO;
import com.mcommings.campaigner.modules.items.services.DiceTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/dicetypes")
public class DiceTypeController {

    private final DiceTypeService diceTypeService;

    @GetMapping
    public List<ViewDiceTypeDTO> getDiceTypes() {

        return diceTypeService.getAll();
    }

    @GetMapping(path = "/{DiceTypeId}")
    public ViewDiceTypeDTO getDiceType(@PathVariable int DiceTypeId) {
        return diceTypeService.getById(DiceTypeId);
    }

    @PostMapping
    public ViewDiceTypeDTO createDiceType(@Valid @RequestBody CreateDiceTypeDTO DiceType) {

        return diceTypeService.create(DiceType);
    }

    @PutMapping
    public ViewDiceTypeDTO updateDiceType(@Valid @RequestBody UpdateDiceTypeDTO DiceType) {
        return diceTypeService.update(DiceType);
    }

    @DeleteMapping(path = "/{DiceTypeId}")
    public void deleteDiceType(@PathVariable int DiceTypeId) {

        diceTypeService.delete(DiceTypeId);
    }
}
