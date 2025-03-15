package com.mcommings.campaigner.modules.items.services.interfaces;

import com.mcommings.campaigner.modules.items.entities.DamageType;

import java.util.List;

public interface IDamageType {

    List<DamageType> getDamageTypes();

    void saveDamageType(DamageType damageType);

    void deleteDamageType(int damageTypeId);

    void updateDamageType(int damageTypeId, DamageType damageType);
}
