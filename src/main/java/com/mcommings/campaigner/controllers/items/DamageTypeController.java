package com.mcommings.campaigner.controllers.items;

import com.mcommings.campaigner.entities.items.DamageType;
import com.mcommings.campaigner.services.items.DamageTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/items/damage-types")
public class DamageTypeController {

    private final DamageTypeService damageTypeService;

    @Autowired
    public DamageTypeController(DamageTypeService damageTypeService) {
        this.damageTypeService = damageTypeService;
    }

    @GetMapping
    public List<DamageType> DamageType() {
        return damageTypeService.getDamageTypes();
    }

    @PostMapping
    public void saveDamageType(@RequestBody DamageType damageType) {
        damageTypeService.saveDamageType(damageType);
    }

    @DeleteMapping(path = "{damageTypeId}")
    public void deleteDamageType(@PathVariable("damageTypeId") int damageTypeId) {
        damageTypeService.deleteDamageType(damageTypeId);
    }

    @PutMapping(path = "{damageTypeId}")
    public void updateDamageType(@PathVariable("damageTypeId") int damageTypeId, @RequestBody DamageType damageType) {
        damageTypeService.updateDamageType(damageTypeId, damageType);
    }
}
