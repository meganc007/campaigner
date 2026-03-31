package com.mcommings.campaigner.modules.items.mappers;

import com.mcommings.campaigner.config.GlobalMapperConfig;
import com.mcommings.campaigner.modules.items.dtos.item_types.CreateItemTypeDTO;
import com.mcommings.campaigner.modules.items.dtos.item_types.UpdateItemTypeDTO;
import com.mcommings.campaigner.modules.items.dtos.item_types.ViewItemTypeDTO;
import com.mcommings.campaigner.modules.items.entities.ItemType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = GlobalMapperConfig.class)
public interface ItemTypeMapper {

    @Mapping(target = "id", ignore = true)
    ItemType toEntity(CreateItemTypeDTO dto);

    ViewItemTypeDTO toDto(ItemType itemType);

    void updateItemTypeFromDto(
            UpdateItemTypeDTO dto,
            @MappingTarget ItemType entity
    );
}
