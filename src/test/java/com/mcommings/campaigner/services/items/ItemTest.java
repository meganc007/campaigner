package com.mcommings.campaigner.services.items;

import com.mcommings.campaigner.modules.items.dtos.ItemDTO;
import com.mcommings.campaigner.modules.items.entities.Item;
import com.mcommings.campaigner.modules.items.mappers.ItemMapper;
import com.mcommings.campaigner.modules.items.repositories.IItemRepository;
import com.mcommings.campaigner.modules.items.services.ItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ItemTest {

    @Mock
    private ItemMapper itemMapper;

    @Mock
    private IItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;

    private Item entity;
    private ItemDTO dto;

    @BeforeEach
    void setUp() {
        Random random = new Random();
        entity = new Item();
        entity.setId(1);
        entity.setName("Test Item");
        entity.setDescription("A fictional item.");
        entity.setFk_campaign_uuid(UUID.randomUUID());
        entity.setRarity("Super rare");
        entity.setGold_value(random.nextInt(100) + 1);
        entity.setSilver_value(random.nextInt(100) + 1);
        entity.setCopper_value(random.nextInt(100) + 1);
        entity.setWeight(10.0f);
        entity.setFk_item_type(random.nextInt(100) + 1);
        entity.setIsMagical(true);
        entity.setIsCursed(false);
        entity.setNotes("This is a note");

        dto = new ItemDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setFk_campaign_uuid(entity.getFk_campaign_uuid());
        dto.setRarity(entity.getRarity());
        dto.setGold_value(entity.getGold_value());
        dto.setSilver_value(entity.getSilver_value());
        dto.setCopper_value(entity.getCopper_value());
        dto.setWeight(entity.getWeight());
        dto.setFk_item_type(entity.getFk_item_type());
        dto.setIsMagical(entity.getIsMagical());
        dto.setIsCursed(entity.getIsCursed());
        dto.setNotes(entity.getNotes());

        when(itemMapper.mapToItemDto(entity)).thenReturn(dto);
        when(itemMapper.mapFromItemDto(dto)).thenReturn(entity);
    }

    @Test
    public void whenThereAreItems_getItems_ReturnsItems() {
        when(itemRepository.findAll()).thenReturn(List.of(entity));
        List<ItemDTO> result = itemService.getItems();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Item", result.get(0).getName());
    }

    @Test
    public void whenThereAreNoItems_getItems_ReturnsEmptyList() {
        when(itemRepository.findAll()).thenReturn(Collections.emptyList());

        List<ItemDTO> result = itemService.getItems();

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected an empty list when there are no items.");
    }

    @Test
    void whenThereIsAItem_getItem_ReturnsItemById() {
        when(itemRepository.findById(1)).thenReturn(Optional.of(entity));

        Optional<ItemDTO> result = itemService.getItem(1);

        assertTrue(result.isPresent());
        assertEquals("Test Item", result.get().getName());
    }

    @Test
    void whenThereIsNotAItem_getItem_ReturnsNothing() {
        when(itemRepository.findById(999)).thenReturn(Optional.empty());

        Optional<ItemDTO> result = itemService.getItem(999);

        assertTrue(result.isEmpty(), "Expected empty Optional when item is not found.");
    }

    @Test
    void whenCampaignUUIDIsValid_getItemsByCampaignUUID_ReturnsItems() {
        UUID campaignUUID = entity.getFk_campaign_uuid();
        when(itemRepository.findByfk_campaign_uuid(campaignUUID)).thenReturn(List.of(entity));

        List<ItemDTO> result = itemService.getItemsByCampaignUUID(campaignUUID);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(campaignUUID, result.get(0).getFk_campaign_uuid());
    }

    @Test
    void whenCampaignUUIDIsInvalid_getItemsByCampaignUUID_ReturnsNothing() {
        UUID campaignUUID = UUID.randomUUID();
        when(itemRepository.findByfk_campaign_uuid(campaignUUID)).thenReturn(Collections.emptyList());

        List<ItemDTO> result = itemService.getItemsByCampaignUUID(campaignUUID);

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected an empty list when no items match the campaign UUID.");
    }

    @Test
    void whenItemIsValid_saveItem_SavesTheItem() {
        when(itemRepository.save(entity)).thenReturn(entity);

        itemService.saveItem(dto);

        verify(itemRepository, times(1)).save(entity);
    }

    @Test
    public void whenItemNameIsInvalid_saveItem_ThrowsIllegalArgumentException() {
        ItemDTO itemWithEmptyName = new ItemDTO();
        itemWithEmptyName.setId(1);
        itemWithEmptyName.setName("");
        itemWithEmptyName.setDescription("A fictional item.");
        itemWithEmptyName.setFk_campaign_uuid(UUID.randomUUID());
        itemWithEmptyName.setRarity("Super rare");
        itemWithEmptyName.setGold_value(1);
        itemWithEmptyName.setSilver_value(1);
        itemWithEmptyName.setCopper_value(1);
        itemWithEmptyName.setWeight(10.0f);
        itemWithEmptyName.setFk_item_type(1);
        itemWithEmptyName.setIsMagical(true);
        itemWithEmptyName.setIsCursed(false);
        itemWithEmptyName.setNotes("This is a note");

        ItemDTO itemWithNullName = new ItemDTO();
        itemWithNullName.setId(1);
        itemWithNullName.setName(null);
        itemWithNullName.setDescription("A fictional item.");
        itemWithNullName.setFk_campaign_uuid(UUID.randomUUID());
        itemWithNullName.setRarity("Super rare");
        itemWithNullName.setGold_value(1);
        itemWithNullName.setSilver_value(1);
        itemWithNullName.setCopper_value(1);
        itemWithNullName.setWeight(10.0f);
        itemWithNullName.setFk_item_type(1);
        itemWithNullName.setIsMagical(true);
        itemWithNullName.setIsCursed(false);
        itemWithNullName.setNotes("This is a note");

        assertThrows(IllegalArgumentException.class, () -> itemService.saveItem(itemWithEmptyName));
        assertThrows(IllegalArgumentException.class, () -> itemService.saveItem(itemWithNullName));
    }

    @Test
    public void whenItemNameAlreadyExists_saveItem_ThrowsDataIntegrityViolationException() {
        when(itemRepository.findByName(dto.getName())).thenReturn(Optional.of(entity));
        assertThrows(DataIntegrityViolationException.class, () -> itemService.saveItem(dto));
        verify(itemRepository, times(1)).findByName(dto.getName());
        verify(itemRepository, never()).save(any(Item.class));
    }

    @Test
    void whenItemIdExists_deleteItem_DeletesTheItem() {
        when(itemRepository.existsById(1)).thenReturn(true);
        itemService.deleteItem(1);
        verify(itemRepository, times(1)).deleteById(1);
    }

    @Test
    void whenItemIdDoesNotExist_deleteItem_ThrowsIllegalArgumentException() {
        when(itemRepository.existsById(999)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> itemService.deleteItem(999));
    }

    @Test
    void whenDeleteItemFails_deleteItem_ThrowsException() {
        when(itemRepository.existsById(1)).thenReturn(true);
        doThrow(new RuntimeException("Database error")).when(itemRepository).deleteById(1);

        assertThrows(RuntimeException.class, () -> itemService.deleteItem(1));
    }

    @Test
    void whenItemIdIsFound_updateItem_UpdatesTheItem() {
        ItemDTO updateDTO = new ItemDTO();
        updateDTO.setName("Updated Name");

        when(itemRepository.findById(1)).thenReturn(Optional.of(entity));
        when(itemRepository.existsById(1)).thenReturn(true);
        when(itemRepository.save(entity)).thenReturn(entity);
        when(itemMapper.mapToItemDto(entity)).thenReturn(updateDTO);

        Optional<ItemDTO> result = itemService.updateItem(1, updateDTO);

        assertTrue(result.isPresent());
        assertEquals("Updated Name", result.get().getName());
    }

    @Test
    void whenItemIdIsNotFound_updateItem_ReturnsEmptyOptional() {
        ItemDTO updateDTO = new ItemDTO();
        updateDTO.setName("Updated Name");

        when(itemRepository.findById(999)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> itemService.updateItem(999, updateDTO));
    }

    @Test
    public void whenItemNameIsInvalid_updateItem_ThrowsIllegalArgumentException() {
        ItemDTO updateEmptyName = new ItemDTO();
        updateEmptyName.setName("");

        ItemDTO updateNullName = new ItemDTO();
        updateNullName.setName(null);

        when(itemRepository.existsById(1)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> itemService.updateItem(1, updateEmptyName));
        assertThrows(IllegalArgumentException.class, () -> itemService.updateItem(1, updateNullName));
    }

    @Test
    public void whenItemNameAlreadyExists_updateItem_ThrowsDataIntegrityViolationException() {
        when(itemRepository.findByName(dto.getName())).thenReturn(Optional.of(entity));

        assertThrows(IllegalArgumentException.class, () -> itemService.updateItem(entity.getId(), dto));
    }
}
