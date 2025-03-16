package com.mcommings.campaigner.modules.items.mappers;

import com.mcommings.campaigner.modules.items.dtos.InventoryDTO;
import com.mcommings.campaigner.modules.items.entities.Inventory;
import org.mapstruct.Mapper;

@Mapper
public interface InventoryMapper {
    Inventory mapFromInventoryDto(InventoryDTO dto);

    InventoryDTO mapToInventoryDto(Inventory inventory);
}
