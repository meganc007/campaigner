package com.mcommings.campaigner.interfaces.items;

import com.mcommings.campaigner.models.items.Weapon;

import java.util.List;

public interface IWeapon {

    List<Weapon> getWeapons();

    void saveWeapon(Weapon weapon);

    void deleteWeapon(int weaponId);

    void updateWeapon(int weaponId, Weapon weapon);
}
