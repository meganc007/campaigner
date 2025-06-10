package com.mcommings.campaigner.modules.overview.facades.mappers;

import com.mcommings.campaigner.modules.items.dtos.*;
import com.mcommings.campaigner.modules.items.entities.*;
import com.mcommings.campaigner.modules.items.mappers.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ItemMapperFacade {

    private final DamageTypeMapper damageTypeMapper;
    private final DiceTypeMapper diceTypeMapper;
    private final InventoryMapper inventoryMapper;
    private final ItemMapper itemMapper;
    private final ItemTypeMapper itemTypeMapper;
    private final WeaponMapper weaponMapper;
    private final WeaponTypeMapper weaponTypeMapper;

    public DamageTypeDTO toDamageTypeDto(DamageType entity) {
        return damageTypeMapper.mapToDamageTypeDto(entity);
    }

    public DiceTypeDTO toDiceTypeDto(DiceType entity) {
        return diceTypeMapper.mapToDiceTypeDto(entity);
    }

    public InventoryDTO toInventoryDto(Inventory entity) {
        return inventoryMapper.mapToInventoryDto(entity);
    }

    public ItemDTO toItemDto(Item entity) {
        return itemMapper.mapToItemDto(entity);
    }

    public ItemTypeDTO toItemTypeDto(ItemType entity) {
        return itemTypeMapper.mapToItemTypeDto(entity);
    }

    public WeaponDTO toWeaponDto(Weapon entity) {
        return weaponMapper.mapToWeaponDto(entity);
    }

    public WeaponTypeDTO toWeaponTypeDto(WeaponType entity) {
        return weaponTypeMapper.mapToWeaponTypeDto(entity);
    }
}
