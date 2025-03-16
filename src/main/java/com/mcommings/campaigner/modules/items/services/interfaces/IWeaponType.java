package com.mcommings.campaigner.modules.items.services.interfaces;

import com.mcommings.campaigner.modules.items.dtos.WeaponTypeDTO;

import java.util.List;
import java.util.Optional;

public interface IWeaponType {

    List<WeaponTypeDTO> getWeaponTypes();

    Optional<WeaponTypeDTO> getWeaponType(int weaponTypeId);

    void saveWeaponType(WeaponTypeDTO weaponType);

    void deleteWeaponType(int weaponTypeId);

    Optional<WeaponTypeDTO> updateWeaponType(int weaponTypeId, WeaponTypeDTO weaponType);
}
