package com.mcommings.campaigner.modules.items.services.interfaces;

import com.mcommings.campaigner.modules.items.dtos.ItemTypeDTO;

import java.util.List;
import java.util.Optional;

public interface IItemType {

    List<ItemTypeDTO> getItemTypes();

    Optional<ItemTypeDTO> getItemType(int itemTypeId);

    void saveItemType(ItemTypeDTO itemType);

    void deleteItemType(int itemTypeId);

    Optional<ItemTypeDTO> updateItemType(int itemTypeId, ItemTypeDTO itemType);
}
