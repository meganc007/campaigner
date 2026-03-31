package com.mcommings.campaigner.modules.items.services;

import com.mcommings.campaigner.config.BaseService;
import com.mcommings.campaigner.modules.common.repositories.ICampaignRepository;
import com.mcommings.campaigner.modules.items.dtos.items.CreateItemDTO;
import com.mcommings.campaigner.modules.items.dtos.items.UpdateItemDTO;
import com.mcommings.campaigner.modules.items.dtos.items.ViewItemDTO;
import com.mcommings.campaigner.modules.items.entities.Item;
import com.mcommings.campaigner.modules.items.mappers.ItemMapper;
import com.mcommings.campaigner.modules.items.repositories.IItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ItemService extends BaseService<
        Item,
        Integer,
        ViewItemDTO,
        CreateItemDTO,
        UpdateItemDTO> {

    private final IItemRepository itemRepository;
    private final ICampaignRepository campaignRepository;
    private final ItemMapper itemMapper;

    @Override
    protected JpaRepository<Item, Integer> getRepository() {
        return itemRepository;
    }

    @Override
    protected ViewItemDTO toViewDto(Item entity) {
        return itemMapper.toDto(entity);
    }

    @Override
    protected Item toEntity(CreateItemDTO dto) {
        Item entity = itemMapper.toEntity(dto);

        entity.setCampaign(
                campaignRepository.getReferenceById(dto.getCampaignUuid())
        );
        return entity;
    }

    @Override
    protected void updateEntity(UpdateItemDTO dto, Item entity) {
        itemMapper.updateItemFromDto(dto, entity);

        if (dto.getCampaignUuid() != null) {
            entity.setCampaign(
                    campaignRepository.getReferenceById(dto.getCampaignUuid())
            );
        }
    }

    @Override
    protected Integer getId(UpdateItemDTO dto) {
        return dto.getId();
    }

    public List<ViewItemDTO> getItemsByCampaignUUID(UUID uuid) {
        return query(itemRepository::findByCampaign_Uuid, uuid);
    }

    public List<ViewItemDTO> getItemsByItemTypeId(int itemTypeId) {
        return query(itemRepository::findByItemType_Id, itemTypeId);
    }
}
