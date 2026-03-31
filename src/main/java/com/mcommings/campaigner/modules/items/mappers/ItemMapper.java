package com.mcommings.campaigner.modules.items.mappers;

import com.mcommings.campaigner.config.GlobalMapperConfig;
import com.mcommings.campaigner.modules.items.dtos.items.CreateItemDTO;
import com.mcommings.campaigner.modules.items.dtos.items.UpdateItemDTO;
import com.mcommings.campaigner.modules.items.dtos.items.ViewItemDTO;
import com.mcommings.campaigner.modules.items.entities.Item;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = GlobalMapperConfig.class)
public interface ItemMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "campaign", ignore = true)
    Item toEntity(CreateItemDTO dto);

    @Mapping(source = "campaign.uuid", target = "campaignUuid")
    @Mapping(source = "itemType.id", target = "itemTypeId")
    ViewItemDTO toDto(Item item);

    void updateItemFromDto(
            UpdateItemDTO dto,
            @MappingTarget Item entity
    );
}
