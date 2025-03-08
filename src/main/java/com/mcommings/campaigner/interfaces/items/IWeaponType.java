package com.mcommings.campaigner.interfaces.items;

import com.mcommings.campaigner.entities.items.WeaponType;

import java.util.List;

public interface IWeaponType {

    List<WeaponType> getWeaponTypes();

    void saveWeaponType(WeaponType weaponType);

    void deleteWeaponType(int weaponTypeId);

    void updateWeaponType(int weaponTypeId, WeaponType weaponType);
}
