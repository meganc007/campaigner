package com.mcommings.campaigner.items.services.interfaces;

import com.mcommings.campaigner.items.entities.DamageType;

import java.util.List;

public interface IDamageType {

    List<DamageType> getDamageTypes();

    void saveDamageType(DamageType damageType);

    void deleteDamageType(int damageTypeId);

    void updateDamageType(int damageTypeId, DamageType damageType);
}
