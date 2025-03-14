package com.mcommings.campaigner.services.items;

import com.mcommings.campaigner.entities.RepositoryHelper;
import com.mcommings.campaigner.entities.items.Inventory;
import com.mcommings.campaigner.interfaces.items.IInventory;
import com.mcommings.campaigner.locations.repositories.IPlaceRepository;
import com.mcommings.campaigner.repositories.items.IInventoryRepository;
import com.mcommings.campaigner.repositories.items.IItemRepository;
import com.mcommings.campaigner.repositories.items.IWeaponRepository;
import com.mcommings.campaigner.repositories.people.IPersonRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static com.mcommings.campaigner.enums.ErrorMessage.*;
import static com.mcommings.campaigner.enums.ForeignKey.*;

@Service
public class InventoryService implements IInventory {

    private final IInventoryRepository inventoryRepository;
    private final IPersonRepository personRepository;
    private final IItemRepository itemRepository;
    private final IWeaponRepository weaponRepository;
    private final IPlaceRepository placeRepository;

    @Autowired
    public InventoryService(IInventoryRepository inventoryRepository, IPersonRepository personRepository,
                            IItemRepository itemRepository, IWeaponRepository weaponRepository, IPlaceRepository placeRepository) {
        this.inventoryRepository = inventoryRepository;
        this.personRepository = personRepository;
        this.itemRepository = itemRepository;
        this.weaponRepository = weaponRepository;
        this.placeRepository = placeRepository;
    }

    @Override
    public List<Inventory> getInventories() {
        return inventoryRepository.findAll();
    }

    @Override
    public List<Inventory> getInventoriesByCampaignUUID(UUID uuid) {
        return inventoryRepository.findByfk_campaign_uuid(uuid);
    }

    @Override
    public List<Inventory> getInventoriesByItem(int itemId) {
        return inventoryRepository.findByfk_item(itemId);
    }

    @Override
    public List<Inventory> getInventoriesByPerson(int personId) {
        return inventoryRepository.findByfk_person(personId);
    }

    @Override
    public List<Inventory> getInventoriesByPlace(int placeId) {
        return inventoryRepository.findByfk_place(placeId);
    }

    @Override
    public List<Inventory> getInventoriesByWeapon(int weaponId) {
        return inventoryRepository.findByfk_weapon(weaponId);
    }

    @Override
    @Transactional
    public void saveInventory(Inventory inventory) throws IllegalArgumentException, DataIntegrityViolationException {
        if (inventoryAlreadyExists(inventory)) {
            throw new DataIntegrityViolationException(INVENTORY_EXISTS.message);
        }
        if (hasForeignKeys(inventory) &&
                RepositoryHelper.foreignKeyIsNotValid(buildReposAndColumnsHashMap(inventory), inventory)) {
            throw new DataIntegrityViolationException(INSERT_FOREIGN_KEY.message);
        }

        inventoryRepository.saveAndFlush(inventory);
    }

    @Override
    @Transactional
    public void deleteInventory(int inventoryId) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(inventoryRepository, inventoryId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
        //TODO: fk check when Inventory is a fk

        inventoryRepository.deleteById(inventoryId);
    }

    @Override
    @Transactional
    public void updateInventory(int inventoryId, Inventory inventory) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(inventoryRepository, inventoryId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        if (hasForeignKeys(inventory) &&
                RepositoryHelper.foreignKeyIsNotValid(buildReposAndColumnsHashMap(inventory), inventory)) {
            throw new DataIntegrityViolationException(INSERT_FOREIGN_KEY.message);
        }
        Inventory inventoryToUpdate = RepositoryHelper.getById(inventoryRepository, inventoryId);
        if (inventory.getFk_person() != null) inventoryToUpdate.setFk_person(inventory.getFk_person());
        if (inventory.getFk_item() != null) inventoryToUpdate.setFk_item(inventory.getFk_item());
        if (inventory.getFk_weapon() != null) inventoryToUpdate.setFk_weapon(inventory.getFk_weapon());
        if (inventory.getFk_place() != null) inventoryToUpdate.setFk_place(inventory.getFk_place());
    }

    private boolean inventoryAlreadyExists(Inventory inventory) {
        return inventoryRepository.inventoryExists(inventory).isPresent();
    }

    private boolean hasForeignKeys(Inventory inventory) {
        return inventory.getFk_person() != null ||
                inventory.getFk_item() != null ||
                inventory.getFk_weapon() != null ||
                inventory.getFk_place() != null;
    }

    private HashMap<CrudRepository, String> buildReposAndColumnsHashMap(Inventory inventory) {
        HashMap<CrudRepository, String> reposAndColumns = new HashMap<>();

        reposAndColumns.put(personRepository, FK_PERSON.columnName);
        reposAndColumns.put(itemRepository, FK_ITEM.columnName);

        if (inventory.getFk_weapon() != null) {
            reposAndColumns.put(weaponRepository, FK_WEAPON.columnName);
        }
        if (inventory.getFk_place() != null) {
            reposAndColumns.put(placeRepository, FK_PLACE.columnName);
        }
        return reposAndColumns;
    }
}
