package com.mcommings.campaigner.items.services.interfaces;

import com.mcommings.campaigner.items.entities.WeaponType;

import java.util.List;

public interface IWeaponType {

    List<WeaponType> getWeaponTypes();

    void saveWeaponType(WeaponType weaponType);

    void deleteWeaponType(int weaponTypeId);

    void updateWeaponType(int weaponTypeId, WeaponType weaponType);
}
