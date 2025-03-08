package com.mcommings.campaigner.controllers.items;

import com.mcommings.campaigner.entities.items.Weapon;
import com.mcommings.campaigner.services.items.WeaponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/items/weapons")
public class WeaponController {

    private final WeaponService weaponService;

    @Autowired
    public WeaponController(WeaponService weaponService) {
        this.weaponService = weaponService;
    }

    @GetMapping
    public List<Weapon> Weapon() {
        return weaponService.getWeapons();
    }

    @GetMapping(path = "/campaign/{uuid}")
    public List<Weapon> getWeaponsByCampaignUUID(UUID uuid) {
        return weaponService.getWeaponsByCampaignUUID(uuid);
    }

    @PostMapping
    public void saveWeapon(@RequestBody Weapon weapon) {
        weaponService.saveWeapon(weapon);
    }

    @DeleteMapping(path = "{weaponId}")
    public void deleteWeapon(@PathVariable("weaponId") int weaponId) {
        weaponService.deleteWeapon(weaponId);
    }

    @PutMapping(path = "{weaponId}")
    public void updateWeapon(@PathVariable("weaponId") int weaponId, @RequestBody Weapon weapon) {
        weaponService.updateWeapon(weaponId, weapon);
    }
}
