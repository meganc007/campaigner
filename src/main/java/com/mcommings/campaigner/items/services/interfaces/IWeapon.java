package com.mcommings.campaigner.items.services.interfaces;

import com.mcommings.campaigner.items.entities.Weapon;

import java.util.List;
import java.util.UUID;

public interface IWeapon {

    List<Weapon> getWeapons();

    List<Weapon> getWeaponsByCampaignUUID(UUID uuid);

    void saveWeapon(Weapon weapon);

    void deleteWeapon(int weaponId);

    void updateWeapon(int weaponId, Weapon weapon);
}
