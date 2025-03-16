package com.mcommings.campaigner.modules.items.mappers;

import com.mcommings.campaigner.modules.items.dtos.ItemTypeDTO;
import com.mcommings.campaigner.modules.items.entities.ItemType;
import org.mapstruct.Mapper;

@Mapper
public interface ItemTypeMapper {
    ItemType mapFromItemTypeDto(ItemTypeDTO dto);

    ItemTypeDTO mapToItemTypeDto(ItemType itemType);
}
