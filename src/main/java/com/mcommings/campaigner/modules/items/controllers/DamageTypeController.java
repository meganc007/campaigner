package com.mcommings.campaigner.modules.items.controllers;

import com.mcommings.campaigner.modules.items.dtos.DamageTypeDTO;
import com.mcommings.campaigner.modules.items.services.interfaces.IDamageType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.mcommings.campaigner.enums.ErrorMessage.ID_NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/damagetypes")
public class DamageTypeController {

    private final IDamageType damageTypeService;

    @GetMapping
    public List<DamageTypeDTO> DamageType() {
        return damageTypeService.getDamageTypes();
    }

    @GetMapping(path = "/{damageTypeId}")
    public DamageTypeDTO getDamageType(@PathVariable("damageTypeId") int damageTypeId) {
        return damageTypeService.getDamageType(damageTypeId).orElseThrow(() -> new IllegalArgumentException(ID_NOT_FOUND.message));
    }

    @PostMapping
    public void saveDamageType(@Valid @RequestBody DamageTypeDTO damageType) {
        damageTypeService.saveDamageType(damageType);
    }

    @DeleteMapping(path = "{damageTypeId}")
    public void deleteDamageType(@PathVariable("damageTypeId") int damageTypeId) {
        damageTypeService.deleteDamageType(damageTypeId);
    }

    @PutMapping(path = "{damageTypeId}")
    public void updateDamageType(@PathVariable("damageTypeId") int damageTypeId, @RequestBody DamageTypeDTO damageType) {
        damageTypeService.updateDamageType(damageTypeId, damageType);
    }
}
