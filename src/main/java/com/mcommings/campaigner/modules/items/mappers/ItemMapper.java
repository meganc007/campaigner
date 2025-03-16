package com.mcommings.campaigner.modules.items.mappers;

import com.mcommings.campaigner.modules.items.dtos.ItemDTO;
import com.mcommings.campaigner.modules.items.entities.Item;
import org.mapstruct.Mapper;

@Mapper
public interface ItemMapper {
    Item mapFromItemDto(ItemDTO dto);

    ItemDTO mapToItemDto(Item item);
}
