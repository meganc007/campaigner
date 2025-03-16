package com.mcommings.campaigner.modules.items.services;

import com.mcommings.campaigner.modules.RepositoryHelper;
import com.mcommings.campaigner.modules.items.dtos.ItemTypeDTO;
import com.mcommings.campaigner.modules.items.mappers.ItemTypeMapper;
import com.mcommings.campaigner.modules.items.repositories.IItemTypeRepository;
import com.mcommings.campaigner.modules.items.services.interfaces.IItemType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.mcommings.campaigner.enums.ErrorMessage.*;

@Service
@RequiredArgsConstructor
public class ItemTypeService implements IItemType {

    private final IItemTypeRepository itemTypeRepository;
    private final ItemTypeMapper itemTypeMapper;

    @Override
    public List<ItemTypeDTO> getItemTypes() {

        return itemTypeRepository.findAll()
                .stream()
                .map(itemTypeMapper::mapToItemTypeDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ItemTypeDTO> getItemType(int itemTypeId) {
        return itemTypeRepository.findById(itemTypeId)
                .map(itemTypeMapper::mapToItemTypeDto);
    }

    @Override
    @Transactional
    public void saveItemType(ItemTypeDTO itemType) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.nameIsNullOrEmpty(itemType)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(itemTypeRepository, itemType.getName())) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }
        itemTypeMapper.mapToItemTypeDto(
                itemTypeRepository.save(itemTypeMapper.mapFromItemTypeDto(itemType)
                ));
    }

    @Override
    @Transactional
    public void deleteItemType(int itemTypeId) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(itemTypeRepository, itemTypeId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
        itemTypeRepository.deleteById(itemTypeId);
    }

    @Override
    @Transactional
    public Optional<ItemTypeDTO> updateItemType(int itemTypeId, ItemTypeDTO itemType) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(itemTypeRepository, itemTypeId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        if (RepositoryHelper.nameIsNullOrEmpty(itemType)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(itemTypeRepository, itemType.getName())) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        return itemTypeRepository.findById(itemTypeId).map(foundItemType -> {
            if (itemType.getName() != null) foundItemType.setName(itemType.getName());
            if (itemType.getDescription() != null) foundItemType.setDescription(itemType.getDescription());

            return itemTypeMapper.mapToItemTypeDto(itemTypeRepository.save(foundItemType));
        });
    }

}
