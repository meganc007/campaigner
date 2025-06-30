package com.mcommings.campaigner.modules.overview.facades.repositories;

import com.mcommings.campaigner.modules.items.entities.*;
import com.mcommings.campaigner.modules.items.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ItemRepositoryFacade {

    private final IDamageTypeRepository damageTypeRepository;
    private final IDiceTypeRepository diceTypeRepository;
    private final IInventoryRepository inventoryRepository;
    private final IItemRepository itemRepository;
    private final IItemTypeRepository itemTypeRepository;
    private final IWeaponRepository weaponRepository;
    private final IWeaponTypeRepository weaponTypeRepository;

    public List<DamageType> findDamageTypes() {
        return damageTypeRepository.findAll();
    }

    public List<DiceType> findDiceTypes() {
        return diceTypeRepository.findAll();
    }

    public List<Inventory> findInventoryByCampaign(UUID uuid) {
        return inventoryRepository.findByfk_campaign_uuid(uuid);
    }

    public List<Item> findItemsByCampaign(UUID uuid) {
        return itemRepository.findByfk_campaign_uuid(uuid);
    }

    public List<ItemType> findItemTypes() {
        return itemTypeRepository.findAllByOrderByNameAsc();
    }

    public List<Weapon> findWeaponsByCampaign(UUID uuid) {
        return weaponRepository.findByfk_campaign_uuid(uuid);
    }

    public List<WeaponType> findWeaponTypes() {
        return weaponTypeRepository.findAll();
    }
}
