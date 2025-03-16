package com.mcommings.campaigner.modules.items.controllers;

import com.mcommings.campaigner.modules.items.dtos.WeaponTypeDTO;
import com.mcommings.campaigner.modules.items.services.interfaces.IWeaponType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.mcommings.campaigner.enums.ErrorMessage.ID_NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/weapontypes")
public class WeaponTypeController {

    private final IWeaponType weaponTypeService;

    @GetMapping
    public List<WeaponTypeDTO> WeaponType() {
        return weaponTypeService.getWeaponTypes();
    }

    @GetMapping(path = "/{weaponTypeId}")
    public WeaponTypeDTO getWeaponType(@PathVariable("weaponTypeId") int weaponTypeId) {
        return weaponTypeService.getWeaponType(weaponTypeId).orElseThrow(() -> new IllegalArgumentException(ID_NOT_FOUND.message));
    }

    @PostMapping
    public void saveWeaponType(@Valid @RequestBody WeaponTypeDTO weaponType) {
        weaponTypeService.saveWeaponType(weaponType);
    }

    @DeleteMapping(path = "{weaponTypeId}")
    public void deleteWeaponType(@PathVariable("weaponTypeId") int weaponTypeId) {
        weaponTypeService.deleteWeaponType(weaponTypeId);
    }

    @PutMapping(path = "{weaponTypeId}")
    public void updateWeaponType(@PathVariable("weaponTypeId") int weaponTypeId, @RequestBody WeaponTypeDTO weaponType) {
        weaponTypeService.updateWeaponType(weaponTypeId, weaponType);
    }

}
