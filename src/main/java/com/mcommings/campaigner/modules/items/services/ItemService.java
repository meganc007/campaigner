package com.mcommings.campaigner.modules.items.services;

import com.mcommings.campaigner.modules.RepositoryHelper;
import com.mcommings.campaigner.modules.items.dtos.ItemDTO;
import com.mcommings.campaigner.modules.items.mappers.ItemMapper;
import com.mcommings.campaigner.modules.items.repositories.IItemRepository;
import com.mcommings.campaigner.modules.items.services.interfaces.IItem;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.mcommings.campaigner.enums.ErrorMessage.*;

@Service
@RequiredArgsConstructor
public class ItemService implements IItem {

    private final IItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Override
    public List<ItemDTO> getItems() {

        return itemRepository.findAll()
                .stream()
                .map(itemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ItemDTO> getItem(int itemId) {
        return itemRepository.findById(itemId)
                .map(itemMapper::mapToItemDto);
    }

    @Override
    public List<ItemDTO> getItemsByCampaignUUID(UUID uuid) {

        return itemRepository.findByfk_campaign_uuid(uuid)
                .stream()
                .map(itemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDTO> getItemsByItemType(int itemTypeId) {
        return itemRepository.findByfk_item_type(itemTypeId)
                .stream()
                .map(itemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void saveItem(ItemDTO item) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.nameIsNullOrEmpty(item)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(itemRepository, item.getName())) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }
        itemMapper.mapToItemDto(
                itemRepository.save(itemMapper.mapFromItemDto(item)
                ));
    }

    @Override
    @Transactional
    public void deleteItem(int itemId) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(itemRepository, itemId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
        itemRepository.deleteById(itemId);
    }

    @Override
    @Transactional
    public Optional<ItemDTO> updateItem(int itemId, ItemDTO item) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(itemRepository, itemId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }
        if (RepositoryHelper.nameIsNullOrEmpty(item)) {
            throw new IllegalArgumentException(NULL_OR_EMPTY.message);
        }
        if (RepositoryHelper.nameAlreadyExists(itemRepository, item.getName())) {
            throw new DataIntegrityViolationException(NAME_EXISTS.message);
        }

        return itemRepository.findById(itemId).map(foundItem -> {
            if (item.getName() != null) foundItem.setName(item.getName());
            if (item.getDescription() != null) foundItem.setDescription(item.getDescription());
            if (item.getFk_campaign_uuid() != null) foundItem.setFk_campaign_uuid(item.getFk_campaign_uuid());
            if (item.getRarity() != null) foundItem.setRarity(item.getRarity());
            if (item.getGold_value() >= 0) foundItem.setGold_value(item.getGold_value());
            if (item.getSilver_value() >= 0) foundItem.setSilver_value(item.getSilver_value());
            if (item.getCopper_value() >= 0) foundItem.setCopper_value(item.getCopper_value());
            if (item.getWeight() >= 0) foundItem.setWeight(item.getWeight());
            if (item.getFk_item_type() != null) foundItem.setFk_item_type(item.getFk_item_type());
            if (item.getIsMagical() != null) foundItem.setIsMagical(item.getIsMagical());
            if (item.getIsCursed() != null) foundItem.setIsCursed(item.getIsCursed());
            if (item.getNotes() != null) foundItem.setNotes(item.getNotes());

            return itemMapper.mapToItemDto(itemRepository.save(foundItem));
        });
    }
}
