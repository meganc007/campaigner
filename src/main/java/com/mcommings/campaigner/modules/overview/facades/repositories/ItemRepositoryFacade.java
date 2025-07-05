package com.mcommings.campaigner.modules.overview.facades.repositories;

import com.mcommings.campaigner.modules.items.entities.*;
import com.mcommings.campaigner.modules.items.repositories.*;
import com.mcommings.campaigner.modules.locations.entities.Place;
import com.mcommings.campaigner.modules.locations.repositories.IPlaceRepository;
import com.mcommings.campaigner.modules.people.entities.Person;
import com.mcommings.campaigner.modules.people.repositories.IPersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
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
    private final IPersonRepository personRepository;
    private final IPlaceRepository placeRepository;

    public List<DamageType> findDamageTypes() {
        return damageTypeRepository.findAllByOrderByNameAsc();
    }

    public List<DiceType> findDiceTypes() {
        return diceTypeRepository.findAllByOrderByNameAsc();
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
        return weaponTypeRepository.findAllByOrderByNameAsc();
    }

    public Optional<Person> findPerson(int id) {
        return personRepository.findById(id);
    }

    public Optional<Place> findPlace(int id) {
        return placeRepository.findById(id);
    }
}
