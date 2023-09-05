package com.mcommings.campaigner.services.items;

import com.mcommings.campaigner.models.items.Item;
import com.mcommings.campaigner.repositories.items.IItemRepository;
import com.mcommings.campaigner.repositories.items.IItemTypeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ItemTest {

    @Mock
    private IItemRepository itemRepository;
    @Mock
    private IItemTypeRepository itemTypeRepository;

    @InjectMocks
    private ItemService itemService;

    @Test
    public void whenThereAreItems_getItems_ReturnsItems() {
        List<Item> items = new ArrayList<>();
        items.add(new Item(1, "Item 1", "Description 1"));
        items.add(new Item(2, "Item 2", "Description 2"));
        items.add(new Item(3, "Item 3", "Description 3", "Rare", 32, 20,
                12, 20.0f, 2, true, false, "Notes"));
        when(itemRepository.findAll()).thenReturn(items);

        List<Item> result = itemService.getItems();

        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals(items, result);
    }

    @Test
    public void whenThereAreNoItems_getItems_ReturnsNothing() {
        List<Item> items = new ArrayList<>();
        when(itemRepository.findAll()).thenReturn(items);

        List<Item> result = itemService.getItems();

        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(items, result);
    }

    @Test
    public void whenItemWithNoForeignKeysIsValid_saveItem_SavesTheItem() {
        Item item = new Item(1, "Item 1", "Description 1");
        when(itemRepository.saveAndFlush(item)).thenReturn(item);

        assertDoesNotThrow(() -> itemService.saveItem(item));

        verify(itemRepository, times(1)).saveAndFlush(item);
    }

    @Test
    public void whenItemWithForeignKeysIsValid_saveItem_SavesTheItem() {
        Item item = new Item(1, "Item 1", "Description 1", "Rare", 32, 20,
                12, 20.0f, 2, true, false, "Notes");

        when(itemTypeRepository.existsById(2)).thenReturn(true);
        when(itemRepository.saveAndFlush(item)).thenReturn(item);

        assertDoesNotThrow(() -> itemService.saveItem(item));

        verify(itemRepository, times(1)).saveAndFlush(item);
    }

    @Test
    public void whenItemNameIsInvalid_saveItem_ThrowsIllegalArgumentException() {
        Item itemWithEmptyName = new Item(1, "", "Description 1");
        Item itemWithNullName = new Item(2, null, "Description 2");

        assertThrows(IllegalArgumentException.class, () -> itemService.saveItem(itemWithEmptyName));
        assertThrows(IllegalArgumentException.class, () -> itemService.saveItem(itemWithNullName));
    }

    @Test
    public void whenItemNameAlreadyExists_saveItem_ThrowsDataIntegrityViolationException() {
        Item item = new Item(1, "Item 1", "Description 3", "Rare", 32, 20,
                12, 20.0f, 2, true, false, "Notes");
        Item itemWithDuplicatedName = new Item(2, "Item 2", "Description 3", "Rare", 32, 20,
                12, 20.0f, 2, true, false, "Notes");

        when(itemRepository.existsById(1)).thenReturn(true);
        when(itemTypeRepository.existsById(2)).thenReturn(true);

        when(itemRepository.saveAndFlush(item)).thenReturn(item);
        when(itemRepository.saveAndFlush(itemWithDuplicatedName)).thenThrow(DataIntegrityViolationException.class);

        assertDoesNotThrow(() -> itemService.saveItem(item));
        assertThrows(DataIntegrityViolationException.class, () -> itemService.saveItem(itemWithDuplicatedName));
    }

    @Test
    public void whenItemHasInvalidForeignKeys_saveItem_ThrowsDataIntegrityViolationException() {
        Item item = new Item(1, "Item 1", "Description 1", "Rare", 32, 20,
                12, 20.0f, 2, true, false, "Notes");


        when(itemTypeRepository.existsById(2)).thenReturn(false);
        when(itemRepository.saveAndFlush(item)).thenReturn(item);

        assertThrows(DataIntegrityViolationException.class, () -> itemService.saveItem(item));
    }

    @Test
    public void whenItemIdExists_deleteItem_DeletesTheItem() {
        int itemId = 1;
        when(itemRepository.existsById(itemId)).thenReturn(true);
        assertDoesNotThrow(() -> itemService.deleteItem(itemId));
        verify(itemRepository, times(1)).deleteById(itemId);
    }

    @Test
    public void whenItemIdDoesNotExist_deleteItem_ThrowsIllegalArgumentException() {
        int itemId = 9000;
        when(itemRepository.existsById(itemId)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> itemService.deleteItem(itemId));
    }

//    TODO: uncomment / fix test when classes that use Item as a fk are added
//    @Test
//    public void whenItemIdIsAForeignKey_deleteItem_ThrowsDataIntegrityViolationException() {
//        int itemId = 1;
//        Place place = new Place(1, "Place", "Description", 1, itemId, 1, 1, 1);
//        List<CrudRepository> repositories = new ArrayList<>(Arrays.asList(placeRepository, eventRepository));
//        List<Place> places = new ArrayList<>(Arrays.asList(place));
//
//        Event event = new Event(1, "Event", "Description", 1, 1, 1, 1, itemId, 1, 1);
//        List<Event> events = new ArrayList<>(Arrays.asList(event));
//
//        when(itemRepository.existsById(itemId)).thenReturn(true);
//        when(placeRepository.findByfk_item(itemId)).thenReturn(places);
//        when(eventRepository.findByfk_item(itemId)).thenReturn(events);
//
//        boolean actual = RepositoryHelper.isForeignKey(repositories, FK_ITEM.columnName, itemId);
//        Assertions.assertTrue(actual);
//        assertThrows(DataIntegrityViolationException.class, () -> itemService.deleteItem(itemId));
//    }

    @Test
    public void whenItemIdWithNoFKIsFound_updateItem_UpdatesTheItem() {
        int itemId = 1;

        Item item = new Item(itemId, "Old Item Name", "Old Description");
        Item updateNoFK = new Item(itemId, "Updated Item Name", "Updated Description");

        when(itemRepository.existsById(itemId)).thenReturn(true);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        itemService.updateItem(itemId, updateNoFK);

        verify(itemRepository).findById(itemId);

        Item result1 = itemRepository.findById(itemId).get();
        Assertions.assertEquals(updateNoFK.getName(), result1.getName());
        Assertions.assertEquals(updateNoFK.getDescription(), result1.getDescription());
    }

    @Test
    public void whenItemIdWithValidFKIsFound_updateItem_UpdatesTheItem() {
        int itemId = 2;

        Item item = new Item(itemId, "Test Item Name", "Test Description", "Rare", 32, 20,
                12, 20.0f, 2, true, false, "Notes");
        Item update = new Item(itemId, "Updated Item Name", "Updated Description", "Rare", 132, 120,
                112, 450.0f, 2, true, true, "Notes");
        List<CrudRepository> repositories = new ArrayList<>(Arrays.asList(itemTypeRepository));

        when(itemRepository.existsById(itemId)).thenReturn(true);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(itemTypeRepository.existsById(2)).thenReturn(true);

        itemService.updateItem(itemId, update);

        verify(itemRepository).findById(itemId);

        Item result = itemRepository.findById(itemId).get();
        Assertions.assertEquals(update.getName(), result.getName());
        Assertions.assertEquals(update.getDescription(), result.getDescription());
        Assertions.assertEquals(update.getDescription(), item.getDescription());
        Assertions.assertEquals(update.getRarity(), item.getRarity());
        Assertions.assertEquals(update.getGold_value(), item.getGold_value());
        Assertions.assertEquals(update.getSilver_value(), item.getSilver_value());
        Assertions.assertEquals(update.getCopper_value(), item.getCopper_value());
        Assertions.assertEquals(update.getWeight(), item.getWeight());
        Assertions.assertEquals(update.getFk_item_type(), item.getFk_item_type());
        Assertions.assertEquals(update.isMagical(), item.isMagical());
        Assertions.assertEquals(update.isCursed(), item.isCursed());
        Assertions.assertEquals(update.getNotes(), item.getNotes());
    }

    @Test
    public void whenItemIdWithInvalidFKIsFound_updateItem_ThrowsDataIntegrityViolationException() {
        int itemId = 2;

        Item item = new Item(itemId, "Test Item Name", "Test Description");
        Item update = new Item(itemId, "Test Item Name", "Test Description", "Rare", 32, 20,
                12, 20.0f, 2, true, false, "Notes");
        List<CrudRepository> repositories = new ArrayList<>(Arrays.asList(itemTypeRepository));

        when(itemRepository.existsById(itemId)).thenReturn(true);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(itemTypeRepository.existsById(2)).thenReturn(false);

        assertThrows(DataIntegrityViolationException.class, () -> itemService.updateItem(itemId, update));
    }

    @Test
    public void whenItemIdIsNotFound_updateItem_ThrowsIllegalArgumentException() {
        int itemId = 1;
        Item item = new Item(itemId, "Old Item Name", "Old Description");

        when(itemRepository.existsById(itemId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> itemService.updateItem(itemId, item));
    }
}
