package com.mcommings.campaigner.modules.items.services;

import com.mcommings.campaigner.config.BaseService;
import com.mcommings.campaigner.modules.items.dtos.item_types.CreateItemTypeDTO;
import com.mcommings.campaigner.modules.items.dtos.item_types.UpdateItemTypeDTO;
import com.mcommings.campaigner.modules.items.dtos.item_types.ViewItemTypeDTO;
import com.mcommings.campaigner.modules.items.entities.ItemType;
import com.mcommings.campaigner.modules.items.mappers.ItemTypeMapper;
import com.mcommings.campaigner.modules.items.repositories.IItemTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemTypeService extends BaseService<
        ItemType,
        Integer,
        ViewItemTypeDTO,
        CreateItemTypeDTO,
        UpdateItemTypeDTO> {

    private final IItemTypeRepository itemTypeRepository;
    private final ItemTypeMapper itemTypeMapper;

    @Override
    protected JpaRepository<ItemType, Integer> getRepository() {
        return itemTypeRepository;
    }

    @Override
    protected ViewItemTypeDTO toViewDto(ItemType entity) {
        return itemTypeMapper.toDto(entity);
    }

    @Override
    protected ItemType toEntity(CreateItemTypeDTO dto) {
        return itemTypeMapper.toEntity(dto);
    }

    @Override
    protected void updateEntity(UpdateItemTypeDTO dto, ItemType entity) {
        itemTypeMapper.updateItemTypeFromDto(dto, entity);
    }

    @Override
    protected Integer getId(UpdateItemTypeDTO dto) {
        return dto.getId();
    }
}
