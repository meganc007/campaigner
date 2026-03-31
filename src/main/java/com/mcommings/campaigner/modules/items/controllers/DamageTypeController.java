package com.mcommings.campaigner.modules.items.controllers;

import com.mcommings.campaigner.modules.items.dtos.damage_types.CreateDamageTypeDTO;
import com.mcommings.campaigner.modules.items.dtos.damage_types.UpdateDamageTypeDTO;
import com.mcommings.campaigner.modules.items.dtos.damage_types.ViewDamageTypeDTO;
import com.mcommings.campaigner.modules.items.services.DamageTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/damagetypes")
public class DamageTypeController {

    private final DamageTypeService damageTypeService;

    @GetMapping
    public List<ViewDamageTypeDTO> getDamageTypes() {

        return damageTypeService.getAll();
    }

    @GetMapping(path = "/{damageTypeId}")
    public ViewDamageTypeDTO getDamageType(@PathVariable int damageTypeId) {
        return damageTypeService.getById(damageTypeId);
    }

    @PostMapping
    public ViewDamageTypeDTO createDamageType(@Valid @RequestBody CreateDamageTypeDTO damageType) {

        return damageTypeService.create(damageType);
    }

    @PutMapping
    public ViewDamageTypeDTO updateDamageType(@Valid @RequestBody UpdateDamageTypeDTO damageType) {
        return damageTypeService.update(damageType);
    }

    @DeleteMapping(path = "/{damageTypeId}")
    public void deleteDamageType(@PathVariable int damageTypeId) {

        damageTypeService.delete(damageTypeId);
    }
}
