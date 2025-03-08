package com.mcommings.campaigner.interfaces.items;

import com.mcommings.campaigner.entities.items.Weapon;

import java.util.List;
import java.util.UUID;

public interface IWeapon {

    List<Weapon> getWeapons();

    List<Weapon> getWeaponsByCampaignUUID(UUID uuid);

    void saveWeapon(Weapon weapon);

    void deleteWeapon(int weaponId);

    void updateWeapon(int weaponId, Weapon weapon);
}
