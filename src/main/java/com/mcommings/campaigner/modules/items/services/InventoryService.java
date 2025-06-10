package com.mcommings.campaigner.modules.items.services;

import com.mcommings.campaigner.modules.RepositoryHelper;
import com.mcommings.campaigner.modules.items.dtos.InventoryDTO;
import com.mcommings.campaigner.modules.items.mappers.InventoryMapper;
import com.mcommings.campaigner.modules.items.repositories.IInventoryRepository;
import com.mcommings.campaigner.modules.items.services.interfaces.IInventory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.mcommings.campaigner.enums.ErrorMessage.DELETE_NOT_FOUND;
import static com.mcommings.campaigner.enums.ErrorMessage.UPDATE_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class InventoryService implements IInventory {

    private final IInventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;

    @Override
    public List<InventoryDTO> getInventories() {

        return inventoryRepository.findAll()
                .stream()
                .map(inventoryMapper::mapToInventoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<InventoryDTO> getInventory(int inventoryId) {
        return inventoryRepository.findById(inventoryId)
                .map(inventoryMapper::mapToInventoryDto);
    }

    @Override
    public List<InventoryDTO> getInventoriesByCampaignUUID(UUID uuid) {
        return inventoryRepository.findByfk_campaign_uuid(uuid)
                .stream()
                .map(inventoryMapper::mapToInventoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<InventoryDTO> getInventoriesByItem(int itemId) {

        return inventoryRepository.findByfk_item(itemId)
                .stream()
                .map(inventoryMapper::mapToInventoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<InventoryDTO> getInventoriesByPerson(int personId) {
        return inventoryRepository.findByfk_person(personId)
                .stream()
                .map(inventoryMapper::mapToInventoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<InventoryDTO> getInventoriesByPlace(int placeId) {

        return inventoryRepository.findByfk_place(placeId)
                .stream()
                .map(inventoryMapper::mapToInventoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<InventoryDTO> getInventoriesByWeapon(int weaponId) {
        return inventoryRepository.findByfk_weapon(weaponId)
                .stream()
                .map(inventoryMapper::mapToInventoryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void saveInventory(InventoryDTO inventory) throws IllegalArgumentException, DataIntegrityViolationException {
        inventoryMapper.mapToInventoryDto(
                inventoryRepository.save(inventoryMapper.mapFromInventoryDto(inventory))
        );
    }

    @Override
    @Transactional
    public void deleteInventory(int inventoryId) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(inventoryRepository, inventoryId)) {
            throw new IllegalArgumentException(DELETE_NOT_FOUND.message);
        }
        inventoryRepository.deleteById(inventoryId);
    }

    @Override
    @Transactional
    public Optional<InventoryDTO> updateInventory(int inventoryId, InventoryDTO inventory) throws IllegalArgumentException, DataIntegrityViolationException {
        if (RepositoryHelper.cannotFindId(inventoryRepository, inventoryId)) {
            throw new IllegalArgumentException(UPDATE_NOT_FOUND.message);
        }

        return inventoryRepository.findById(inventoryId).map(foundInventory -> {
            if (inventory.getFk_campaign_uuid() != null)
                foundInventory.setFk_campaign_uuid(inventory.getFk_campaign_uuid());
            if (inventory.getFk_person() != null) foundInventory.setFk_person(inventory.getFk_person());
            if (inventory.getFk_item() != null) foundInventory.setFk_item(inventory.getFk_item());
            if (inventory.getFk_weapon() != null) foundInventory.setFk_weapon(inventory.getFk_weapon());
            if (inventory.getFk_place() != null) foundInventory.setFk_place(inventory.getFk_place());

            return inventoryMapper.mapToInventoryDto(inventoryRepository.save(foundInventory));
        });
    }
}
