package com.mcommings.campaigner.services.items;

import com.mcommings.campaigner.interfaces.items.IItemType;
import com.mcommings.campaigner.models.RepositoryHelper;
import com.mcommings.campaigner.models.items.ItemType;
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
import static com.mcommings.campaigner.enums.ForeignKey.FK_ITEM_TYPE;

@Service
public class ItemTypeService implements IItemType {

    private final IItemTypeRepository itemTypeRepository;
    private final IItemRepository itemRepository;

    @Autowired
    public ItemTypeService(IItemTypeRepository itemTypeRepository, IItemRepository itemRepository) {
        this.itemTypeRepository = itemTypeRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public List<ItemType> getItemTypes() {
        return itemTypeRepository.findAll();
    }

    @Override
    @Transactional
    public void saveItemType(ItemType itemType) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.nameIsNullOrEmpty(itemType)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(itemTypeRepository, itemType)) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        itemTypeRepository.saveAndFlush(itemType);
    }

    @Override
    @Transactional
    public void deleteItemType(int itemTypeId) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(itemTypeRepository, itemTypeId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
        if (RepositoryHelper.isForeignKey(getReposWhereItemTypeIsAForeignKey(), FK_ITEM_TYPE.columnName, itemTypeId)) {
            throw new DataIntegrityViolationException(DELETE_FOREIGN_KEY.message);
        }

        itemTypeRepository.deleteById(itemTypeId);
    }

    @Override
    @Transactional
    public void updateItemType(int itemTypeId, ItemType itemType) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(itemTypeRepository, itemTypeId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        ItemType itemTypeToUpdate = RepositoryHelper.getById(itemTypeRepository, itemTypeId);
        if (itemType.getName() != null) itemTypeToUpdate.setName(itemType.getName());
        if (itemType.getDescription() != null) itemTypeToUpdate.setDescription(itemType.getDescription());
    }

    private List<CrudRepository> getReposWhereItemTypeIsAForeignKey() {
        return new ArrayList<>(Arrays.asList(itemRepository));
    }
}
