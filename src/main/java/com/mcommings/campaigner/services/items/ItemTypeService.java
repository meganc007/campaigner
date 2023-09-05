package com.mcommings.campaigner.services.items;

import com.mcommings.campaigner.interfaces.items.IItemType;
import com.mcommings.campaigner.models.RepositoryHelper;
import com.mcommings.campaigner.models.items.ItemType;
import com.mcommings.campaigner.repositories.items.IItemTypeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.mcommings.campaigner.enums.ErrorMessage.*;

@Service
public class ItemTypeService implements IItemType {

    private final IItemTypeRepository itemTypeRepository;

    @Autowired
    public ItemTypeService(IItemTypeRepository itemTypeRepository) {
        this.itemTypeRepository = itemTypeRepository;
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
// TODO: uncomment when class that uses ItemType as a fk is added
//        if (RepositoryHelper.isForeignKey(getReposWhereItemTypeIsAForeignKey(), FK_ITEM_TYPE.columnName, itemTypeId)) {
//            throw new DataIntegrityViolationException(DELETE_FOREIGN_KEY.message);
//        }

        itemTypeRepository.deleteById(itemTypeId);
    }

    @Override
    @Transactional
    public void updateItemType(int itemTypeId, ItemType itemType) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(itemTypeRepository, itemTypeId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        ItemType itemTypeToUpdate = RepositoryHelper.getById(itemTypeRepository, itemTypeId);
        itemTypeToUpdate.setName(itemType.getName());
        itemTypeToUpdate.setDescription(itemType.getDescription());
    }

// TODO: uncomment when class that uses ItemType as a fk is added
//    private List<CrudRepository> getReposWhereItemTypeIsAForeignKey() {
//        return new ArrayList<>(Arrays.asList());
//    }
}
