package com.mcommings.campaigner.modules.items.controllers;

import com.mcommings.campaigner.modules.items.dtos.WeaponDTO;
import com.mcommings.campaigner.modules.items.services.interfaces.IWeapon;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.mcommings.campaigner.enums.ErrorMessage.ID_NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/weapons")
public class WeaponController {

    private final IWeapon weaponService;

    @GetMapping
    public List<WeaponDTO> getWeapons() {
        return weaponService.getWeapons();
    }

    @GetMapping(path = "/{weaponId}")
    public WeaponDTO getWeapon(@PathVariable("weaponId") int weaponId) {
        return weaponService.getWeapon(weaponId).orElseThrow(() -> new IllegalArgumentException(ID_NOT_FOUND.message));
    }

    @GetMapping(path = "/campaign/{uuid}")
    public List<WeaponDTO> getWeaponsByCampaignUUID(UUID uuid) {
        return weaponService.getWeaponsByCampaignUUID(uuid);
    }

    @PostMapping
    public void saveWeapon(@Valid @RequestBody WeaponDTO weapon) {
        weaponService.saveWeapon(weapon);
    }

    @DeleteMapping(path = "{weaponId}")
    public void deleteWeapon(@PathVariable("weaponId") int weaponId) {
        weaponService.deleteWeapon(weaponId);
    }

    @PutMapping(path = "{weaponId}")
    public void updateWeapon(@PathVariable("weaponId") int weaponId, @RequestBody WeaponDTO weapon) {
        weaponService.updateWeapon(weaponId, weapon);
    }
}
