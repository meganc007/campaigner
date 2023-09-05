package com.mcommings.campaigner.services.items;

import com.mcommings.campaigner.models.RepositoryHelper;
import com.mcommings.campaigner.models.items.Item;
import com.mcommings.campaigner.models.items.ItemType;
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

import static com.mcommings.campaigner.enums.ForeignKey.FK_ITEM_TYPE;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ItemTypeTest {

    @Mock
    private IItemTypeRepository itemTypeRepository;
    @Mock
    private IItemRepository itemRepository;

    @InjectMocks
    private ItemTypeService itemTypeService;

    @Test
    public void whenThereAreItemTypes_getItemTypes_ReturnsItemTypes() {
        List<ItemType> itemTypes = new ArrayList<>();
        itemTypes.add(new ItemType(1, "ItemType 1", "Description 1"));
        itemTypes.add(new ItemType(2, "ItemType 2", "Description 2"));

        when(itemTypeRepository.findAll()).thenReturn(itemTypes);

        List<ItemType> result = itemTypeService.getItemTypes();

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(itemTypes, result);
    }

    @Test
    public void whenThereAreNoItemTypes_getItemTypes_ReturnsNothing() {
        List<ItemType> itemTypes = new ArrayList<>();
        when(itemTypeRepository.findAll()).thenReturn(itemTypes);

        List<ItemType> result = itemTypeService.getItemTypes();

        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(itemTypes, result);
    }

    @Test
    public void whenItemTypeIsValid_saveItemType_SavesTheItemType() {
        ItemType itemType = new ItemType(1, "ItemType 1", "Description 1");
        when(itemTypeRepository.saveAndFlush(itemType)).thenReturn(itemType);

        assertDoesNotThrow(() -> itemTypeService.saveItemType(itemType));
        verify(itemTypeRepository, times(1)).saveAndFlush(itemType);
    }

    @Test
    public void whenItemTypeNameIsInvalid_saveItemType_ThrowsIllegalArgumentException() {
        ItemType itemTypeWithEmptyName = new ItemType(1, "", "Description 1");
        ItemType itemTypeWithNullName = new ItemType(2, null, "Description 2");

        assertThrows(IllegalArgumentException.class, () -> itemTypeService.saveItemType(itemTypeWithEmptyName));
        assertThrows(IllegalArgumentException.class, () -> itemTypeService.saveItemType(itemTypeWithNullName));
    }

    @Test
    public void whenItemTypeNameAlreadyExists_saveItemType_ThrowsDataIntegrityViolationException() {
        ItemType itemType = new ItemType(1, "ItemType 1", "Description 1");
        ItemType itemTypeWithDuplicatedName = new ItemType(2, "ItemType 1", "Description 2");
        when(itemTypeRepository.saveAndFlush(itemType)).thenReturn(itemType);
        when(itemTypeRepository.saveAndFlush(itemTypeWithDuplicatedName)).thenThrow(DataIntegrityViolationException.class);

        assertDoesNotThrow(() -> itemTypeService.saveItemType(itemType));
        assertThrows(DataIntegrityViolationException.class, () -> itemTypeService.saveItemType(itemTypeWithDuplicatedName));
    }

    @Test
    public void whenItemTypeIdExists_deleteItemType_DeletesTheItemType() {
        int itemTypeId = 1;
        when(itemTypeRepository.existsById(itemTypeId)).thenReturn(true);
        assertDoesNotThrow(() -> itemTypeService.deleteItemType(itemTypeId));
        verify(itemTypeRepository, times(1)).deleteById(itemTypeId);
    }

    @Test
    public void whenItemTypeIdDoesNotExist_deleteItemType_ThrowsIllegalArgumentException() {
        int itemTypeId = 9000;
        when(itemTypeRepository.existsById(itemTypeId)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> itemTypeService.deleteItemType(itemTypeId));
    }

    @Test
    public void whenItemTypeIdIsAForeignKey_deleteItemType_ThrowsDataIntegrityViolationException() {
        int itemTypeId = 1;
        Item item = new Item(1, "Item 1", "Description 1", "Rare", 32, 20,
                12, 20.0f, 2, true, false, "Notes");
        List<CrudRepository> repositories = new ArrayList<>(Arrays.asList(itemRepository));
        List<Item> items = new ArrayList<>(Arrays.asList(item));

        when(itemTypeRepository.existsById(itemTypeId)).thenReturn(true);
        when(itemRepository.findByfk_item_type(itemTypeId)).thenReturn(items);

        boolean actual = RepositoryHelper.isForeignKey(repositories, FK_ITEM_TYPE.columnName, itemTypeId);
        Assertions.assertTrue(actual);
        assertThrows(DataIntegrityViolationException.class, () -> itemTypeService.deleteItemType(itemTypeId));
    }

    @Test
    public void whenItemTypeIdIsFound_updateItemType_UpdatesTheItemType() {
        int itemTypeId = 1;
        ItemType itemType = new ItemType(itemTypeId, "Old ItemType Name", "Old Description");
        ItemType itemTypeToUpdate = new ItemType(itemTypeId, "Updated ItemType Name", "Updated Description");

        when(itemTypeRepository.existsById(itemTypeId)).thenReturn(true);
        when(itemTypeRepository.findById(itemTypeId)).thenReturn(Optional.of(itemType));

        itemTypeService.updateItemType(itemTypeId, itemTypeToUpdate);

        verify(itemTypeRepository).findById(itemTypeId);

        ItemType result = itemTypeRepository.findById(itemTypeId).get();
        Assertions.assertEquals(itemTypeToUpdate.getName(), result.getName());
        Assertions.assertEquals(itemTypeToUpdate.getDescription(), result.getDescription());
    }

    @Test
    public void whenItemTypeIdIsNotFound_updateItemType_ThrowsIllegalArgumentException() {
        int itemTypeId = 1;
        ItemType itemType = new ItemType(itemTypeId, "Old ItemType Name", "Old Description");

        when(itemTypeRepository.existsById(itemTypeId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> itemTypeService.updateItemType(itemTypeId, itemType));
    }
}
