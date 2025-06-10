package com.mcommings.campaigner.services.items;

import com.mcommings.campaigner.modules.items.dtos.InventoryDTO;
import com.mcommings.campaigner.modules.items.entities.Inventory;
import com.mcommings.campaigner.modules.items.mappers.InventoryMapper;
import com.mcommings.campaigner.modules.items.repositories.IInventoryRepository;
import com.mcommings.campaigner.modules.items.services.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class InventoryTest {

    @Mock
    private InventoryMapper inventoryMapper;

    @Mock
    private IInventoryRepository inventoryRepository;

    @InjectMocks
    private InventoryService inventoryService;

    private Inventory entity;
    private InventoryDTO dto;

    @BeforeEach
    void setUp() {
        Random random = new Random();
        entity = new Inventory();
        entity.setId(1);
        entity.setFk_campaign_uuid(UUID.randomUUID());
        entity.setFk_person(random.nextInt(100) + 1);
        entity.setFk_item(random.nextInt(100) + 1);
        entity.setFk_weapon(random.nextInt(100) + 1);
        entity.setFk_place(random.nextInt(100) + 1);

        dto = new InventoryDTO();
        dto.setId(entity.getId());
        dto.setFk_campaign_uuid(entity.getFk_campaign_uuid());
        dto.setFk_person(entity.getFk_person());
        dto.setFk_item(entity.getFk_item());
        dto.setFk_weapon(entity.getFk_weapon());
        dto.setFk_place(entity.getFk_place());

        when(inventoryMapper.mapToInventoryDto(entity)).thenReturn(dto);
        when(inventoryMapper.mapFromInventoryDto(dto)).thenReturn(entity);
    }

    @Test
    public void whenThereAreInventories_getInventories_ReturnsInventories() {
        when(inventoryRepository.findAll()).thenReturn(List.of(entity));
        List<InventoryDTO> result = inventoryService.getInventories();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void whenThereAreNoInventories_getInventories_ReturnsEmptyList() {
        when(inventoryRepository.findAll()).thenReturn(Collections.emptyList());

        List<InventoryDTO> result = inventoryService.getInventories();

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected an empty list when there are no inventories.");
    }

    @Test
    void whenThereIsAInventory_getInventory_ReturnsInventoryById() {
        when(inventoryRepository.findById(1)).thenReturn(Optional.of(entity));

        Optional<InventoryDTO> result = inventoryService.getInventory(1);

        assertTrue(result.isPresent());
    }

    @Test
    void whenThereIsNotAInventory_getInventory_ReturnsNothing() {
        when(inventoryRepository.findById(999)).thenReturn(Optional.empty());

        Optional<InventoryDTO> result = inventoryService.getInventory(999);

        assertTrue(result.isEmpty(), "Expected empty Optional when inventory is not found.");
    }

    @Test
    void whenCampaignUUIDIsValid_getInventoriesByCampaignUUID_ReturnsInventories() {
        UUID campaignUUID = entity.getFk_campaign_uuid();
        when(inventoryRepository.findByfk_campaign_uuid(campaignUUID)).thenReturn(List.of(entity));

        List<InventoryDTO> result = inventoryService.getInventoriesByCampaignUUID(campaignUUID);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(campaignUUID, result.get(0).getFk_campaign_uuid());
    }

    @Test
    void whenCampaignUUIDIsInvalid_getInventoriesByCampaignUUID_ReturnsNothing() {
        UUID campaignUUID = UUID.randomUUID();
        when(inventoryRepository.findByfk_campaign_uuid(campaignUUID)).thenReturn(Collections.emptyList());

        List<InventoryDTO> result = inventoryService.getInventoriesByCampaignUUID(campaignUUID);

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected an empty list when no inventories match the campaign UUID.");
    }

    @Test
    void whenInventoryIsValid_saveInventory_SavesTheInventory() {
        when(inventoryRepository.save(entity)).thenReturn(entity);

        inventoryService.saveInventory(dto);

        verify(inventoryRepository, times(1)).save(entity);
    }

    @Test
    void whenInventoryIdExists_deleteInventory_DeletesTheInventory() {
        when(inventoryRepository.existsById(1)).thenReturn(true);
        inventoryService.deleteInventory(1);
        verify(inventoryRepository, times(1)).deleteById(1);
    }

    @Test
    void whenInventoryIdDoesNotExist_deleteInventory_ThrowsIllegalArgumentException() {
        when(inventoryRepository.existsById(999)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> inventoryService.deleteInventory(999));
    }

    @Test
    void whenDeleteInventoryFails_deleteInventory_ThrowsException() {
        when(inventoryRepository.existsById(1)).thenReturn(true);
        doThrow(new RuntimeException("Database error")).when(inventoryRepository).deleteById(1);

        assertThrows(RuntimeException.class, () -> inventoryService.deleteInventory(1));
    }

    @Test
    void whenInventoryIdIsFound_updateInventory_UpdatesTheInventory() {
        InventoryDTO updateDTO = new InventoryDTO();

        when(inventoryRepository.findById(1)).thenReturn(Optional.of(entity));
        when(inventoryRepository.existsById(1)).thenReturn(true);
        when(inventoryRepository.save(entity)).thenReturn(entity);
        when(inventoryMapper.mapToInventoryDto(entity)).thenReturn(updateDTO);

        Optional<InventoryDTO> result = inventoryService.updateInventory(1, updateDTO);

        assertTrue(result.isPresent());
    }

    @Test
    void whenInventoryIdIsNotFound_updateInventory_ReturnsEmptyOptional() {
        InventoryDTO updateDTO = new InventoryDTO();

        when(inventoryRepository.findById(999)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> inventoryService.updateInventory(999, updateDTO));
    }
}
