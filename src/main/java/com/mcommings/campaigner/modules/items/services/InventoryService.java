package com.mcommings.campaigner.modules.items.services;

import com.mcommings.campaigner.config.BaseService;
import com.mcommings.campaigner.modules.common.repositories.ICampaignRepository;
import com.mcommings.campaigner.modules.items.dtos.inventories.CreateInventoryDTO;
import com.mcommings.campaigner.modules.items.dtos.inventories.UpdateInventoryDTO;
import com.mcommings.campaigner.modules.items.dtos.inventories.ViewInventoryDTO;
import com.mcommings.campaigner.modules.items.entities.Inventory;
import com.mcommings.campaigner.modules.items.mappers.InventoryMapper;
import com.mcommings.campaigner.modules.items.repositories.IInventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InventoryService extends BaseService<
        Inventory,
        Integer,
        ViewInventoryDTO,
        CreateInventoryDTO,
        UpdateInventoryDTO> {

    private final IInventoryRepository inventoryRepository;
    private final ICampaignRepository campaignRepository;
    private final InventoryMapper inventoryMapper;

    @Override
    protected JpaRepository<Inventory, Integer> getRepository() {
        return inventoryRepository;
    }

    @Override
    protected ViewInventoryDTO toViewDto(Inventory entity) {
        return inventoryMapper.toDto(entity);
    }

    @Override
    protected Inventory toEntity(CreateInventoryDTO dto) {
        Inventory entity = inventoryMapper.toEntity(dto);

        entity.setCampaign(
                campaignRepository.getReferenceById(dto.getCampaignUuid())
        );
        return entity;
    }

    @Override
    protected void updateEntity(UpdateInventoryDTO dto, Inventory entity) {
        inventoryMapper.updateInventoryFromDto(dto, entity);

        if (dto.getCampaignUuid() != null) {
            entity.setCampaign(
                    campaignRepository.getReferenceById(dto.getCampaignUuid())
            );
        }
    }

    @Override
    protected Integer getId(UpdateInventoryDTO dto) {
        return dto.getId();
    }

    public List<ViewInventoryDTO> getInventoriesByCampaignUUID(UUID uuid) {
        return query(inventoryRepository::findByCampaign_Uuid, uuid);
    }

    public List<ViewInventoryDTO> getInventoriesByPersonId(int personId) {
        return query(inventoryRepository::findByPerson_Id, personId);
    }

    public List<ViewInventoryDTO> getInventoriesByItemId(int itemId) {
        return query(inventoryRepository::findByItem_Id, itemId);
    }

    public List<ViewInventoryDTO> getInventoriesByWeaponId(int weaponId) {
        return query(inventoryRepository::findByWeapon_Id, weaponId);
    }

    public List<ViewInventoryDTO> getInventoriesByPlaceId(int placeId) {
        return query(inventoryRepository::findByPlace_Id, placeId);
    }
}
