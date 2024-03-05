package com.mcommings.campaigner.services.items;

import com.mcommings.campaigner.models.items.Inventory;
import com.mcommings.campaigner.repositories.items.IInventoryRepository;
import com.mcommings.campaigner.repositories.items.IItemRepository;
import com.mcommings.campaigner.repositories.items.IWeaponRepository;
import com.mcommings.campaigner.repositories.locations.IPlaceRepository;
import com.mcommings.campaigner.repositories.people.IPersonRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class InventoryTest {

    @Mock
    private IInventoryRepository inventoryRepository;
    @Mock
    private IPersonRepository personRepository;
    @Mock
    private IItemRepository itemRepository;
    @Mock
    private IWeaponRepository weaponRepository;
    @Mock
    private IPlaceRepository placeRepository;

    @InjectMocks
    private InventoryService inventoryService;

    @Test
    public void whenThereAreInventories_getInventories_ReturnsInventories() {
        List<Inventory> inventories = new ArrayList<>();
        UUID campaign = UUID.randomUUID();
        inventories.add(new Inventory(1, 1, 2, 3, 4, campaign));
        inventories.add(new Inventory(2, 2, 3, 4, 5, campaign));
        inventories.add(new Inventory(3, 3, 4, 5, 6, campaign));
        when(inventoryRepository.findAll()).thenReturn(inventories);

        List<Inventory> result = inventoryService.getInventories();

        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals(inventories, result);
    }

    @Test
    public void whenThereAreNoInventories_getInventories_ReturnsNothing() {
        List<Inventory> inventories = new ArrayList<>();
        when(inventoryRepository.findAll()).thenReturn(inventories);

        List<Inventory> result = inventoryService.getInventories();

        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(inventories, result);
    }

    @Test
    public void whenCampaignUUIDIsValid_getInventoriesByCampaignUUID_ReturnsInventories() {
        List<Inventory> inventories = new ArrayList<>();
        UUID campaign = UUID.randomUUID();
        inventories.add(new Inventory(1, 1, 2, 3, 4, campaign));
        inventories.add(new Inventory(2, 2, 3, 4, 5, campaign));
        inventories.add(new Inventory(3, 3, 4, 5, 6, campaign));
        when(inventoryRepository.findByfk_campaign_uuid(campaign)).thenReturn(inventories);

        List<Inventory> results = inventoryService.getInventoriesByCampaignUUID(campaign);

        Assertions.assertEquals(3, results.size());
        Assertions.assertEquals(inventories, results);
    }

    @Test
    public void whenCampaignUUIDIsInvalid_getInventoriesByCampaignUUID_ReturnsNothing() {
        UUID campaign = UUID.randomUUID();
        List<Inventory> inventories = new ArrayList<>();
        when(inventoryRepository.findByfk_campaign_uuid(campaign)).thenReturn(inventories);

        List<Inventory> result = inventoryService.getInventoriesByCampaignUUID(campaign);

        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(inventories, result);
    }

    @Test
    public void whenInventoryIsValid_saveInventory_SavesTheInventory() {
        Inventory inventory = new Inventory(1, 2, 3, 4, 5, UUID.randomUUID());

        when(personRepository.existsById(2)).thenReturn(true);
        when(itemRepository.existsById(3)).thenReturn(true);
        when(weaponRepository.existsById(4)).thenReturn(true);
        when(placeRepository.existsById(5)).thenReturn(true);
        when(inventoryRepository.saveAndFlush(inventory)).thenReturn(inventory);

        assertDoesNotThrow(() -> inventoryService.saveInventory(inventory));

        verify(inventoryRepository, times(1)).saveAndFlush(inventory);
    }

    @Test
    public void whenInventoryAlreadyExists_saveInventory_ThrowsDataIntegrityViolationException() {
        Inventory inventory = new Inventory(1, 2, 3, 4, 5, UUID.randomUUID());
        Inventory inventoryCopy = new Inventory(2, 2, 3, 4, 5, UUID.randomUUID());

        when(personRepository.existsById(2)).thenReturn(true);
        when(itemRepository.existsById(3)).thenReturn(true);
        when(weaponRepository.existsById(4)).thenReturn(true);
        when(placeRepository.existsById(5)).thenReturn(true);

        when(inventoryRepository.saveAndFlush(inventory)).thenReturn(inventory);
        when(inventoryRepository.saveAndFlush(inventoryCopy)).thenThrow(DataIntegrityViolationException.class);

        assertDoesNotThrow(() -> inventoryService.saveInventory(inventory));
        assertThrows(DataIntegrityViolationException.class, () -> inventoryService.saveInventory(inventoryCopy));
    }

    @Test
    public void whenInventoryIdExists_deleteInventory_DeletesTheInventory() {
        int inventoryId = 1;
        when(inventoryRepository.existsById(inventoryId)).thenReturn(true);
        assertDoesNotThrow(() -> inventoryService.deleteInventory(inventoryId));
        verify(inventoryRepository, times(1)).deleteById(inventoryId);
    }

    @Test
    public void whenInventoryIdDoesNotExist_deleteInventory_ThrowsIllegalArgumentException() {
        int inventoryId = 9000;
        when(inventoryRepository.existsById(inventoryId)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> inventoryService.deleteInventory(inventoryId));
    }

    //TODO: test delete when Inventory is a fk

    @Test
    public void whenInventoryIdWithValidFKIsFound_updateInventory_UpdatesTheInventory() {
        int id = 1;
        UUID campaign = UUID.randomUUID();

        Inventory inventory = new Inventory(id, 2, 3, 4, 5, campaign);
        Inventory update = new Inventory(id, 3, 4, 5, 6, campaign);

        when(inventoryRepository.existsById(id)).thenReturn(true);
        when(inventoryRepository.findById(id)).thenReturn(Optional.of(inventory));
        when(personRepository.existsById(2)).thenReturn(true);
        when(itemRepository.existsById(3)).thenReturn(true);
        when(weaponRepository.existsById(4)).thenReturn(true);
        when(placeRepository.existsById(5)).thenReturn(true);

        when(personRepository.existsById(3)).thenReturn(true);
        when(itemRepository.existsById(4)).thenReturn(true);
        when(weaponRepository.existsById(5)).thenReturn(true);
        when(placeRepository.existsById(6)).thenReturn(true);

        inventoryService.updateInventory(id, update);

        verify(inventoryRepository).findById(id);

        Inventory result = inventoryRepository.findById(id).get();
        Assertions.assertEquals(update.getId(), result.getId());
        Assertions.assertEquals(update.getFk_person(), result.getFk_person());
        Assertions.assertEquals(update.getFk_item(), result.getFk_item());
        Assertions.assertEquals(update.getFk_weapon(), result.getFk_weapon());
        Assertions.assertEquals(update.getFk_place(), result.getFk_place());
    }

    @Test
    public void whenInventoryIdWithInvalidFKIsFound_updateInventory_ThrowsDataIntegrityViolationException() {
        int id = 1;
        UUID campaign = UUID.randomUUID();

        Inventory inventory = new Inventory(id, 2, 3, 4, 5, campaign);
        Inventory update = new Inventory(id, 3, 4, 5, 6, campaign);

        when(inventoryRepository.existsById(id)).thenReturn(true);
        when(inventoryRepository.findById(id)).thenReturn(Optional.of(inventory));
        when(personRepository.existsById(2)).thenReturn(true);
        when(itemRepository.existsById(3)).thenReturn(false);
        when(weaponRepository.existsById(4)).thenReturn(false);
        when(placeRepository.existsById(5)).thenReturn(true);

        when(personRepository.existsById(3)).thenReturn(false);
        when(itemRepository.existsById(4)).thenReturn(true);
        when(weaponRepository.existsById(5)).thenReturn(false);
        when(placeRepository.existsById(6)).thenReturn(false);

        assertThrows(DataIntegrityViolationException.class, () -> inventoryService.updateInventory(id, update));
    }

    @Test
    public void whenInventoryIdIsNotFound_updateInventory_ThrowsIllegalArgumentException() {
        int id = 1;
        Inventory inventory = new Inventory(id, 2, 3, 4, 5, UUID.randomUUID());

        when(inventoryRepository.existsById(id)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> inventoryService.updateInventory(id, inventory));
    }

    @Test
    public void whenSomeInventoryFieldsChanged_updateInventory_OnlyUpdatesChangedFields() {
        int inventoryId = 1;
        Inventory inventory = new Inventory(inventoryId, 1, 1, 2, 3, UUID.randomUUID());

        int newPerson = 3;

        Inventory inventoryToUpdate = new Inventory();
        inventoryToUpdate.setFk_person(newPerson);
        inventoryToUpdate.setFk_item(1);

        when(inventoryRepository.existsById(inventoryId)).thenReturn(true);
        when(personRepository.existsById(1)).thenReturn(true);
        when(personRepository.existsById(3)).thenReturn(true);
        when(itemRepository.existsById(1)).thenReturn(true);
        when(weaponRepository.existsById(2)).thenReturn(true);
        when(placeRepository.existsById(3)).thenReturn(true);
        when(inventoryRepository.findById(inventoryId)).thenReturn(Optional.of(inventory));

        inventoryService.updateInventory(inventoryId, inventoryToUpdate);

        verify(inventoryRepository).findById(inventoryId);

        Inventory result = inventoryRepository.findById(inventoryId).get();
        Assertions.assertEquals(newPerson, result.getFk_person());
        Assertions.assertEquals(inventory.getFk_item(), result.getFk_item());
        Assertions.assertEquals(inventory.getFk_weapon(), result.getFk_weapon());
        Assertions.assertEquals(inventory.getFk_place(), result.getFk_place());
    }

}
