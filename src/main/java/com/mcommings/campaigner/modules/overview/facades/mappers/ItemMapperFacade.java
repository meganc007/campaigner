package com.mcommings.campaigner.modules.overview.facades.mappers;

import com.mcommings.campaigner.modules.items.dtos.*;
import com.mcommings.campaigner.modules.items.entities.*;
import com.mcommings.campaigner.modules.items.mappers.*;
import com.mcommings.campaigner.modules.locations.dtos.PlaceDTO;
import com.mcommings.campaigner.modules.locations.entities.Place;
import com.mcommings.campaigner.modules.locations.mappers.PlaceMapper;
import com.mcommings.campaigner.modules.people.dtos.PersonDTO;
import com.mcommings.campaigner.modules.people.entities.Person;
import com.mcommings.campaigner.modules.people.mappers.PersonMapper;
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
    private final PersonMapper personMapper;
    private final PlaceMapper placeMapper;

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

    public PersonDTO toPersonDto(Person entity) {
        return personMapper.mapToPersonDto(entity);
    }

    public PlaceDTO toPlaceDto(Place entity) {
        return placeMapper.mapToPlaceDto(entity);
    }
}
