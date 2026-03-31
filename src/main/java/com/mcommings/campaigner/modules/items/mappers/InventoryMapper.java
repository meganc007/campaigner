package com.mcommings.campaigner.modules.items.mappers;

import com.mcommings.campaigner.config.GlobalMapperConfig;
import com.mcommings.campaigner.modules.items.dtos.inventories.CreateInventoryDTO;
import com.mcommings.campaigner.modules.items.dtos.inventories.UpdateInventoryDTO;
import com.mcommings.campaigner.modules.items.dtos.inventories.ViewInventoryDTO;
import com.mcommings.campaigner.modules.items.entities.Inventory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = GlobalMapperConfig.class)
public interface InventoryMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "campaign", ignore = true)
    Inventory toEntity(CreateInventoryDTO dto);

    @Mapping(source = "campaign.uuid", target = "campaignUuid")
    @Mapping(source = "person.id", target = "personId")
    @Mapping(source = "item.id", target = "itemId")
    @Mapping(source = "weapon.id", target = "weaponId")
    @Mapping(source = "place.id", target = "placeId")
    ViewInventoryDTO toDto(Inventory inventory);

    void updateInventoryFromDto(
            UpdateInventoryDTO dto,
            @MappingTarget Inventory entity
    );
}
