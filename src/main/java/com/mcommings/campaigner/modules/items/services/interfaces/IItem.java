package com.mcommings.campaigner.modules.items.services.interfaces;

import com.mcommings.campaigner.modules.items.dtos.ItemDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IItem {

    List<ItemDTO> getItems();

    Optional<ItemDTO> getItem(int itemId);

    List<ItemDTO> getItemsByCampaignUUID(UUID uuid);

    void saveItem(ItemDTO item);

    void deleteItem(int itemId);

    Optional<ItemDTO> updateItem(int itemId, ItemDTO item);
}
