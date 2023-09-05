package com.mcommings.campaigner.services.items;

import com.mcommings.campaigner.interfaces.items.IItem;
import com.mcommings.campaigner.models.RepositoryHelper;
import com.mcommings.campaigner.models.items.Item;
import com.mcommings.campaigner.repositories.items.IItemRepository;
import com.mcommings.campaigner.repositories.items.IItemTypeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mcommings.campaigner.enums.ErrorMessage.*;

@Service
public class ItemService implements IItem {

    private final IItemRepository itemRepository;
    private final IItemTypeRepository itemTypeRepository;

    @Autowired
    public ItemService(IItemRepository itemRepository, IItemTypeRepository itemTypeRepository) {
        this.itemRepository = itemRepository;
        this.itemTypeRepository = itemTypeRepository;
    }

    @Override
    public List<Item> getItems() {
        return itemRepository.findAll();
    }

    @Override
    @Transactional
    public void saveItem(Item item) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.nameIsNullOrEmpty(item)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(itemRepository, item)) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }
        if (hasForeignKeys(item) &&
                RepositoryHelper.foreignKeyIsNotValid(itemRepository, getListOfForeignKeyRepositories(), item)) {
            throw new DataIntegrityViolationException(INSERT_FOREIGN_KEY.message);
        }

        itemRepository.saveAndFlush(item);
    }

    @Override
    @Transactional
    public void deleteItem(int itemId) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(itemRepository, itemId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
// TODO: uncomment when class that uses Item as a fk is added
//        if (RepositoryHelper.isForeignKey(getReposWhereItemIsAForeignKey(), FK_ITEM.columnName, itemId)) {
//            throw new DataIntegrityViolationException(DELETE_FOREIGN_KEY.message);
//        }

        itemRepository.deleteById(itemId);
    }

    @Override
    @Transactional
    public void updateItem(int itemId, Item item) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(itemRepository, itemId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        if (hasForeignKeys(item) &&
                RepositoryHelper.foreignKeyIsNotValid(itemRepository, getListOfForeignKeyRepositories(), item)) {
            throw new DataIntegrityViolationException(UPDATE_FOREIGN_KEY.message);
        }
        Item itemToUpdate = RepositoryHelper.getById(itemRepository, itemId);
        itemToUpdate.setName(item.getName());
        itemToUpdate.setDescription(item.getDescription());
        itemToUpdate.setRarity(item.getRarity());
        itemToUpdate.setGold_value(item.getGold_value());
        itemToUpdate.setSilver_value(item.getSilver_value());
        itemToUpdate.setCopper_value(item.getCopper_value());
        itemToUpdate.setWeight(item.getWeight());
        itemToUpdate.setFk_item_type(item.getFk_item_type());
        itemToUpdate.setMagical(item.isMagical());
        itemToUpdate.setCursed(item.isCursed());
        itemToUpdate.setNotes(item.getNotes());
    }

// TODO: uncomment when class that uses Item as a fk is added
//    private List<CrudRepository> getReposWhereItemIsAForeignKey() {
//        return new ArrayList<>(Arrays.asList());
//    }

    private boolean hasForeignKeys(Item item) {
        return item.getFk_item_type() != null;
    }

    private List<CrudRepository> getListOfForeignKeyRepositories() {
        return new ArrayList<>(Arrays.asList(itemTypeRepository));
    }
}
