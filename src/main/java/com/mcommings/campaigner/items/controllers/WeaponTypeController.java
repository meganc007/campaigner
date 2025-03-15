package com.mcommings.campaigner.items.controllers;

import com.mcommings.campaigner.items.entities.WeaponType;
import com.mcommings.campaigner.items.services.WeaponTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/items/weapon-types")
public class WeaponTypeController {

    private final WeaponTypeService weaponTypeService;

    @Autowired
    public WeaponTypeController(WeaponTypeService weaponTypeService) {
        this.weaponTypeService = weaponTypeService;
    }

    @GetMapping
    public List<WeaponType> WeaponType() {
        return weaponTypeService.getWeaponTypes();
    }

    @PostMapping
    public void saveWeaponType(@RequestBody WeaponType weaponType) {
        weaponTypeService.saveWeaponType(weaponType);
    }

    @DeleteMapping(path = "{weaponTypeId}")
    public void deleteWeaponType(@PathVariable("weaponTypeId") int weaponTypeId) {
        weaponTypeService.deleteWeaponType(weaponTypeId);
    }

    @PutMapping(path = "{weaponTypeId}")
    public void updateWeaponType(@PathVariable("weaponTypeId") int weaponTypeId, @RequestBody WeaponType weaponType) {
        weaponTypeService.updateWeaponType(weaponTypeId, weaponType);
    }

}
