package com.mcommings.campaigner.modules.items.controllers;

import com.mcommings.campaigner.modules.items.dtos.weapons.CreateWeaponDTO;
import com.mcommings.campaigner.modules.items.dtos.weapons.UpdateWeaponDTO;
import com.mcommings.campaigner.modules.items.dtos.weapons.ViewWeaponDTO;
import com.mcommings.campaigner.modules.items.services.WeaponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/weapons")
public class WeaponController {

    private final WeaponService weaponService;

    @GetMapping
    public List<ViewWeaponDTO> getWeapons() {

        return weaponService.getAll();
    }

    @GetMapping(path = "/{weaponId}")
    public ViewWeaponDTO getWeapon(@PathVariable int weaponId) {
        return weaponService.getById(weaponId);
    }

    @GetMapping(path = "/campaign/{uuid}")
    public List<ViewWeaponDTO> getWeaponsByCampaignUUID(@PathVariable UUID uuid) {
        return weaponService.getWeaponsByCampaignUUID(uuid);
    }

    @GetMapping(path = "/weaponType/{weaponTypeId}")
    public List<ViewWeaponDTO> getWeaponsByWeaponTypeId(@PathVariable int weaponTypeId) {
        return weaponService.getWeaponsByWeaponTypeId(weaponTypeId);
    }

    @GetMapping(path = "/damageType/{damageTypeId}")
    public List<ViewWeaponDTO> getWeaponsByDamageTypeId(@PathVariable int damageTypeId) {
        return weaponService.getWeaponsByDamageTypeId(damageTypeId);
    }

    @GetMapping(path = "/diceType/{diceTypeId}")
    public List<ViewWeaponDTO> getWeaponsByDiceTypeId(@PathVariable int diceTypeId) {
        return weaponService.getWeaponsByDiceTypeId(diceTypeId);
    }

    @PostMapping
    public ViewWeaponDTO createWeapon(@Valid @RequestBody CreateWeaponDTO weapon) {

        return weaponService.create(weapon);
    }

    @PutMapping
    public ViewWeaponDTO updateWeapon(@Valid @RequestBody UpdateWeaponDTO weapon) {
        return weaponService.update(weapon);
    }

    @DeleteMapping(path = "/{weaponId}")
    public void deleteWeapon(@PathVariable int weaponId) {

        weaponService.delete(weaponId);
    }
}
