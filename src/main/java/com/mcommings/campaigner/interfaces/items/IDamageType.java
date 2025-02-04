package com.mcommings.campaigner.interfaces.items;

import com.mcommings.campaigner.entities.items.DamageType;

import java.util.List;

public interface IDamageType {

    List<DamageType> getDamageTypes();

    void saveDamageType(DamageType damageType);

    void deleteDamageType(int damageTypeId);

    void updateDamageType(int damageTypeId, DamageType damageType);
}
