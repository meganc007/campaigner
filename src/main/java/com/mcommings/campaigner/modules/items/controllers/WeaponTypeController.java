package com.mcommings.campaigner.modules.items.controllers;

import com.mcommings.campaigner.modules.items.dtos.weapon_types.CreateWeaponTypeDTO;
import com.mcommings.campaigner.modules.items.dtos.weapon_types.UpdateWeaponTypeDTO;
import com.mcommings.campaigner.modules.items.dtos.weapon_types.ViewWeaponTypeDTO;
import com.mcommings.campaigner.modules.items.services.WeaponTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/weapontypes")
public class WeaponTypeController {

    private final WeaponTypeService weaponTypeService;

    @GetMapping
    public List<ViewWeaponTypeDTO> getWeaponTypes() {

        return weaponTypeService.getAll();
    }

    @GetMapping(path = "/{weaponTypeId}")
    public ViewWeaponTypeDTO getWeaponType(@PathVariable int weaponTypeId) {
        return weaponTypeService.getById(weaponTypeId);
    }

    @PostMapping
    public ViewWeaponTypeDTO createWeaponType(@Valid @RequestBody CreateWeaponTypeDTO weaponType) {

        return weaponTypeService.create(weaponType);
    }

    @PutMapping
    public ViewWeaponTypeDTO updateWeaponType(@Valid @RequestBody UpdateWeaponTypeDTO weaponType) {
        return weaponTypeService.update(weaponType);
    }

    @DeleteMapping(path = "/{weaponTypeId}")
    public void deleteWeaponType(@PathVariable int weaponTypeId) {

        weaponTypeService.delete(weaponTypeId);
    }
}
