package com.mcommings.campaigner.services.items;

import com.mcommings.campaigner.modules.items.dtos.WeaponTypeDTO;
import com.mcommings.campaigner.modules.items.entities.WeaponType;
import com.mcommings.campaigner.modules.items.mappers.WeaponTypeMapper;
import com.mcommings.campaigner.modules.items.repositories.IWeaponTypeRepository;
import com.mcommings.campaigner.modules.items.services.WeaponTypeService;
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
public class WeaponTypeTest {

    @Mock
    private WeaponTypeMapper weaponTypeMapper;

    @Mock
    private IWeaponTypeRepository weaponTypeRepository;

    @InjectMocks
    private WeaponTypeService weaponTypeService;

    private WeaponType entity;
    private WeaponTypeDTO dto;

    @BeforeEach
    void setUp() {
        Random random = new Random();
        entity = new WeaponType();
        entity.setId(1);
        entity.setName("Test WeaponType");
        entity.setDescription("A fictional weaponType.");

        dto = new WeaponTypeDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());

        when(weaponTypeMapper.mapToWeaponTypeDto(entity)).thenReturn(dto);
        when(weaponTypeMapper.mapFromWeaponTypeDto(dto)).thenReturn(entity);
    }

    @Test
    public void whenThereAreWeaponTypes_getWeaponTypes_ReturnsWeaponTypes() {
        when(weaponTypeRepository.findAll()).thenReturn(List.of(entity));
        List<WeaponTypeDTO> result = weaponTypeService.getWeaponTypes();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test WeaponType", result.get(0).getName());
    }

    @Test
    public void whenThereAreNoWeaponTypes_getWeaponTypes_ReturnsEmptyList() {
        when(weaponTypeRepository.findAll()).thenReturn(Collections.emptyList());

        List<WeaponTypeDTO> result = weaponTypeService.getWeaponTypes();

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected an empty list when there are no weaponTypes.");
    }

    @Test
    void whenThereIsAWeaponType_getWeaponType_ReturnsWeaponTypeById() {
        when(weaponTypeRepository.findById(1)).thenReturn(Optional.of(entity));

        Optional<WeaponTypeDTO> result = weaponTypeService.getWeaponType(1);

        assertTrue(result.isPresent());
        assertEquals("Test WeaponType", result.get().getName());
    }

    @Test
    void whenThereIsNotAWeaponType_getWeaponType_ReturnsNothing() {
        when(weaponTypeRepository.findById(999)).thenReturn(Optional.empty());

        Optional<WeaponTypeDTO> result = weaponTypeService.getWeaponType(999);

        assertTrue(result.isEmpty(), "Expected empty Optional when weaponType is not found.");
    }

    @Test
    void whenWeaponTypeIsValid_saveWeaponType_SavesTheWeaponType() {
        when(weaponTypeRepository.save(entity)).thenReturn(entity);

        weaponTypeService.saveWeaponType(dto);

        verify(weaponTypeRepository, times(1)).save(entity);
    }

    @Test
    public void whenWeaponTypeNameIsInvalid_saveWeaponType_ThrowsIllegalArgumentException() {
        WeaponTypeDTO weaponTypeWithEmptyName = new WeaponTypeDTO();
        weaponTypeWithEmptyName.setId(1);
        weaponTypeWithEmptyName.setName("");
        weaponTypeWithEmptyName.setDescription("A fictional weaponType.");

        WeaponTypeDTO weaponTypeWithNullName = new WeaponTypeDTO();
        weaponTypeWithNullName.setId(1);
        weaponTypeWithNullName.setName(null);
        weaponTypeWithNullName.setDescription("A fictional weaponType.");

        assertThrows(IllegalArgumentException.class, () -> weaponTypeService.saveWeaponType(weaponTypeWithEmptyName));
        assertThrows(IllegalArgumentException.class, () -> weaponTypeService.saveWeaponType(weaponTypeWithNullName));
    }

    @Test
    public void whenWeaponTypeNameAlreadyExists_saveWeaponType_ThrowsDataIntegrityViolationException() {
        when(weaponTypeRepository.findByName(dto.getName())).thenReturn(Optional.of(entity));
        assertThrows(DataIntegrityViolationException.class, () -> weaponTypeService.saveWeaponType(dto));
        verify(weaponTypeRepository, times(1)).findByName(dto.getName());
        verify(weaponTypeRepository, never()).save(any(WeaponType.class));
    }

    @Test
    void whenWeaponTypeIdExists_deleteWeaponType_DeletesTheWeaponType() {
        when(weaponTypeRepository.existsById(1)).thenReturn(true);
        weaponTypeService.deleteWeaponType(1);
        verify(weaponTypeRepository, times(1)).deleteById(1);
    }

    @Test
    void whenWeaponTypeIdDoesNotExist_deleteWeaponType_ThrowsIllegalArgumentException() {
        when(weaponTypeRepository.existsById(999)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> weaponTypeService.deleteWeaponType(999));
    }

    @Test
    void whenDeleteWeaponTypeFails_deleteWeaponType_ThrowsException() {
        when(weaponTypeRepository.existsById(1)).thenReturn(true);
        doThrow(new RuntimeException("Database error")).when(weaponTypeRepository).deleteById(1);

        assertThrows(RuntimeException.class, () -> weaponTypeService.deleteWeaponType(1));
    }

    @Test
    void whenWeaponTypeIdIsFound_updateWeaponType_UpdatesTheWeaponType() {
        WeaponTypeDTO updateDTO = new WeaponTypeDTO();
        updateDTO.setName("Updated Name");

        when(weaponTypeRepository.findById(1)).thenReturn(Optional.of(entity));
        when(weaponTypeRepository.existsById(1)).thenReturn(true);
        when(weaponTypeRepository.save(entity)).thenReturn(entity);
        when(weaponTypeMapper.mapToWeaponTypeDto(entity)).thenReturn(updateDTO);

        Optional<WeaponTypeDTO> result = weaponTypeService.updateWeaponType(1, updateDTO);

        assertTrue(result.isPresent());
        assertEquals("Updated Name", result.get().getName());
    }

    @Test
    void whenWeaponTypeIdIsNotFound_updateWeaponType_ReturnsEmptyOptional() {
        WeaponTypeDTO updateDTO = new WeaponTypeDTO();
        updateDTO.setName("Updated Name");

        when(weaponTypeRepository.findById(999)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> weaponTypeService.updateWeaponType(999, updateDTO));
    }

    @Test
    public void whenWeaponTypeNameIsInvalid_updateWeaponType_ThrowsIllegalArgumentException() {
        WeaponTypeDTO updateEmptyName = new WeaponTypeDTO();
        updateEmptyName.setName("");

        WeaponTypeDTO updateNullName = new WeaponTypeDTO();
        updateNullName.setName(null);

        when(weaponTypeRepository.existsById(1)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> weaponTypeService.updateWeaponType(1, updateEmptyName));
        assertThrows(IllegalArgumentException.class, () -> weaponTypeService.updateWeaponType(1, updateNullName));
    }

    @Test
    public void whenWeaponTypeNameAlreadyExists_updateWeaponType_ThrowsDataIntegrityViolationException() {
        when(weaponTypeRepository.findByName(dto.getName())).thenReturn(Optional.of(entity));

        assertThrows(IllegalArgumentException.class, () -> weaponTypeService.updateWeaponType(entity.getId(), dto));
    }
}
