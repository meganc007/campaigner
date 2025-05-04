package com.mcommings.campaigner.modules.items.services.interfaces;

import com.mcommings.campaigner.modules.items.dtos.WeaponDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IWeapon {

    List<WeaponDTO> getWeapons();

    Optional<WeaponDTO> getWeapon(int weaponId);

    List<WeaponDTO> getWeaponsByCampaignUUID(UUID uuid);

    List<WeaponDTO> getWeaponsByWeaponType(int weaponTypeId);

    List<WeaponDTO> getWeaponsByDamageType(int damageTypeId);

    List<WeaponDTO> getWeaponsByDiceType(int diceTypeId);

    void saveWeapon(WeaponDTO weapon);

    void deleteWeapon(int weaponId);

    Optional<WeaponDTO> updateWeapon(int weaponId, WeaponDTO weapon);
}
