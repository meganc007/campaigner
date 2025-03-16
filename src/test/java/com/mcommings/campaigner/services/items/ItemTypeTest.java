package com.mcommings.campaigner.services.items;

import com.mcommings.campaigner.modules.items.dtos.ItemTypeDTO;
import com.mcommings.campaigner.modules.items.entities.ItemType;
import com.mcommings.campaigner.modules.items.mappers.ItemTypeMapper;
import com.mcommings.campaigner.modules.items.repositories.IItemTypeRepository;
import com.mcommings.campaigner.modules.items.services.ItemTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ItemTypeTest {

    @Mock
    private ItemTypeMapper itemTypeMapper;

    @Mock
    private IItemTypeRepository itemTypeRepository;

    @InjectMocks
    private ItemTypeService itemTypeService;

    private ItemType entity;
    private ItemTypeDTO dto;

    @BeforeEach
    void setUp() {
        Random random = new Random();
        entity = new ItemType();
        entity.setId(1);
        entity.setName("Test ItemType");
        entity.setDescription("A fictional itemType.");

        dto = new ItemTypeDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());

        when(itemTypeMapper.mapToItemTypeDto(entity)).thenReturn(dto);
        when(itemTypeMapper.mapFromItemTypeDto(dto)).thenReturn(entity);
    }

    @Test
    public void whenThereAreItemTypes_getItemTypes_ReturnsItemTypes() {
        when(itemTypeRepository.findAll()).thenReturn(List.of(entity));
        List<ItemTypeDTO> result = itemTypeService.getItemTypes();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test ItemType", result.get(0).getName());
    }

    @Test
    public void whenThereAreNoItemTypes_getItemTypes_ReturnsEmptyList() {
        when(itemTypeRepository.findAll()).thenReturn(Collections.emptyList());

        List<ItemTypeDTO> result = itemTypeService.getItemTypes();

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected an empty list when there are no itemTypes.");
    }

    @Test
    void whenThereIsAItemType_getItemType_ReturnsItemTypeById() {
        when(itemTypeRepository.findById(1)).thenReturn(Optional.of(entity));

        Optional<ItemTypeDTO> result = itemTypeService.getItemType(1);

        assertTrue(result.isPresent());
        assertEquals("Test ItemType", result.get().getName());
    }

    @Test
    void whenThereIsNotAItemType_getItemType_ReturnsNothing() {
        when(itemTypeRepository.findById(999)).thenReturn(Optional.empty());

        Optional<ItemTypeDTO> result = itemTypeService.getItemType(999);

        assertTrue(result.isEmpty(), "Expected empty Optional when itemType is not found.");
    }

    @Test
    void whenItemTypeIsValid_saveItemType_SavesTheItemType() {
        when(itemTypeRepository.save(entity)).thenReturn(entity);

        itemTypeService.saveItemType(dto);

        verify(itemTypeRepository, times(1)).save(entity);
    }

    @Test
    public void whenItemTypeNameIsInvalid_saveItemType_ThrowsIllegalArgumentException() {
        ItemTypeDTO itemTypeWithEmptyName = new ItemTypeDTO();
        itemTypeWithEmptyName.setId(1);
        itemTypeWithEmptyName.setName("");
        itemTypeWithEmptyName.setDescription("A fictional itemType.");

        ItemTypeDTO itemTypeWithNullName = new ItemTypeDTO();
        itemTypeWithNullName.setId(1);
        itemTypeWithNullName.setName(null);
        itemTypeWithNullName.setDescription("A fictional itemType.");

        assertThrows(IllegalArgumentException.class, () -> itemTypeService.saveItemType(itemTypeWithEmptyName));
        assertThrows(IllegalArgumentException.class, () -> itemTypeService.saveItemType(itemTypeWithNullName));
    }

    @Test
    public void whenItemTypeNameAlreadyExists_saveItemType_ThrowsDataIntegrityViolationException() {
        when(itemTypeRepository.findByName(dto.getName())).thenReturn(Optional.of(entity));
        assertThrows(DataIntegrityViolationException.class, () -> itemTypeService.saveItemType(dto));
        verify(itemTypeRepository, times(1)).findByName(dto.getName());
        verify(itemTypeRepository, never()).save(any(ItemType.class));
    }

    @Test
    void whenItemTypeIdExists_deleteItemType_DeletesTheItemType() {
        when(itemTypeRepository.existsById(1)).thenReturn(true);
        itemTypeService.deleteItemType(1);
        verify(itemTypeRepository, times(1)).deleteById(1);
    }

    @Test
    void whenItemTypeIdDoesNotExist_deleteItemType_ThrowsIllegalArgumentException() {
        when(itemTypeRepository.existsById(999)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> itemTypeService.deleteItemType(999));
    }

    @Test
    void whenDeleteItemTypeFails_deleteItemType_ThrowsException() {
        when(itemTypeRepository.existsById(1)).thenReturn(true);
        doThrow(new RuntimeException("Database error")).when(itemTypeRepository).deleteById(1);

        assertThrows(RuntimeException.class, () -> itemTypeService.deleteItemType(1));
    }

    @Test
    void whenItemTypeIdIsFound_updateItemType_UpdatesTheItemType() {
        ItemTypeDTO updateDTO = new ItemTypeDTO();
        updateDTO.setName("Updated Name");

        when(itemTypeRepository.findById(1)).thenReturn(Optional.of(entity));
        when(itemTypeRepository.existsById(1)).thenReturn(true);
        when(itemTypeRepository.save(entity)).thenReturn(entity);
        when(itemTypeMapper.mapToItemTypeDto(entity)).thenReturn(updateDTO);

        Optional<ItemTypeDTO> result = itemTypeService.updateItemType(1, updateDTO);

        assertTrue(result.isPresent());
        assertEquals("Updated Name", result.get().getName());
    }

    @Test
    void whenItemTypeIdIsNotFound_updateItemType_ReturnsEmptyOptional() {
        ItemTypeDTO updateDTO = new ItemTypeDTO();
        updateDTO.setName("Updated Name");

        when(itemTypeRepository.findById(999)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> itemTypeService.updateItemType(999, updateDTO));
    }

    @Test
    public void whenItemTypeNameIsInvalid_updateItemType_ThrowsIllegalArgumentException() {
        ItemTypeDTO updateEmptyName = new ItemTypeDTO();
        updateEmptyName.setName("");

        ItemTypeDTO updateNullName = new ItemTypeDTO();
        updateNullName.setName(null);

        when(itemTypeRepository.existsById(1)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> itemTypeService.updateItemType(1, updateEmptyName));
        assertThrows(IllegalArgumentException.class, () -> itemTypeService.updateItemType(1, updateNullName));
    }

    @Test
    public void whenItemTypeNameAlreadyExists_updateItemType_ThrowsDataIntegrityViolationException() {
        when(itemTypeRepository.findByName(dto.getName())).thenReturn(Optional.of(entity));

        assertThrows(IllegalArgumentException.class, () -> itemTypeService.updateItemType(entity.getId(), dto));
    }
}
