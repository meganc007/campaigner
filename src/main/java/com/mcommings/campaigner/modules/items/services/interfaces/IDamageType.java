package com.mcommings.campaigner.modules.items.services.interfaces;

import com.mcommings.campaigner.modules.items.dtos.DamageTypeDTO;

import java.util.List;
import java.util.Optional;

public interface IDamageType {

    List<DamageTypeDTO> getDamageTypes();

    Optional<DamageTypeDTO> getDamageType(int damageTypeId);

    void saveDamageType(DamageTypeDTO damageType);

    void deleteDamageType(int damageTypeId);

    Optional<DamageTypeDTO> updateDamageType(int damageTypeId, DamageTypeDTO damageType);
}
