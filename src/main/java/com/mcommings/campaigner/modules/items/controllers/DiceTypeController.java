package com.mcommings.campaigner.modules.items.controllers;

import com.mcommings.campaigner.modules.items.dtos.DiceTypeDTO;
import com.mcommings.campaigner.modules.items.services.interfaces.IDiceType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.mcommings.campaigner.enums.ErrorMessage.ID_NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/dicetypes")
public class DiceTypeController {

    private final IDiceType diceTypeService;

    @GetMapping
    public List<DiceTypeDTO> DiceType() {
        return diceTypeService.getDiceTypes();
    }

    @GetMapping(path = "/{diceTypeId}")
    public DiceTypeDTO getDiceType(@PathVariable("diceTypeId") int diceTypeId) {
        return diceTypeService.getDiceType(diceTypeId).orElseThrow(() -> new IllegalArgumentException(ID_NOT_FOUND.message));
    }

    @PostMapping
    public void saveDiceType(@Valid @RequestBody DiceTypeDTO diceType) {
        diceTypeService.saveDiceType(diceType);
    }

    @DeleteMapping(path = "{diceTypeId}")
    public void deleteDiceType(@PathVariable("diceTypeId") int diceTypeId) {
        diceTypeService.deleteDiceType(diceTypeId);
    }

    @PutMapping(path = "{diceTypeId}")
    public void updateDiceType(@PathVariable("diceTypeId") int diceTypeId, @RequestBody DiceTypeDTO diceType) {
        diceTypeService.updateDiceType(diceTypeId, diceType);
    }
}
