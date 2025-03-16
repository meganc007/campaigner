package com.mcommings.campaigner.services.items;

import com.mcommings.campaigner.modules.items.dtos.DamageTypeDTO;
import com.mcommings.campaigner.modules.items.entities.DamageType;
import com.mcommings.campaigner.modules.items.mappers.DamageTypeMapper;
import com.mcommings.campaigner.modules.items.repositories.IDamageTypeRepository;
import com.mcommings.campaigner.modules.items.services.DamageTypeService;
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
public class DamageTypeTest {

    @Mock
    private DamageTypeMapper damageTypeMapper;

    @Mock
    private IDamageTypeRepository damageTypeRepository;

    @InjectMocks
    private DamageTypeService damageTypeService;

    private DamageType entity;
    private DamageTypeDTO dto;

    @BeforeEach
    void setUp() {
        Random random = new Random();
        entity = new DamageType();
        entity.setId(1);
        entity.setName("Test DamageType");
        entity.setDescription("A fictional damageType.");

        dto = new DamageTypeDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());

        when(damageTypeMapper.mapToDamageTypeDto(entity)).thenReturn(dto);
        when(damageTypeMapper.mapFromDamageTypeDto(dto)).thenReturn(entity);
    }

    @Test
    public void whenThereAreDamageTypes_getDamageTypes_ReturnsDamageTypes() {
        when(damageTypeRepository.findAll()).thenReturn(List.of(entity));
        List<DamageTypeDTO> result = damageTypeService.getDamageTypes();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test DamageType", result.get(0).getName());
    }

    @Test
    public void whenThereAreNoDamageTypes_getDamageTypes_ReturnsEmptyList() {
        when(damageTypeRepository.findAll()).thenReturn(Collections.emptyList());

        List<DamageTypeDTO> result = damageTypeService.getDamageTypes();

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected an empty list when there are no damageTypes.");
    }

    @Test
    void whenThereIsADamageType_getDamageType_ReturnsDamageTypeById() {
        when(damageTypeRepository.findById(1)).thenReturn(Optional.of(entity));

        Optional<DamageTypeDTO> result = damageTypeService.getDamageType(1);

        assertTrue(result.isPresent());
        assertEquals("Test DamageType", result.get().getName());
    }

    @Test
    void whenThereIsNotADamageType_getDamageType_ReturnsNothing() {
        when(damageTypeRepository.findById(999)).thenReturn(Optional.empty());

        Optional<DamageTypeDTO> result = damageTypeService.getDamageType(999);

        assertTrue(result.isEmpty(), "Expected empty Optional when damageType is not found.");
    }

    @Test
    void whenDamageTypeIsValid_saveDamageType_SavesTheDamageType() {
        when(damageTypeRepository.save(entity)).thenReturn(entity);

        damageTypeService.saveDamageType(dto);

        verify(damageTypeRepository, times(1)).save(entity);
    }

    @Test
    public void whenDamageTypeNameIsInvalid_saveDamageType_ThrowsIllegalArgumentException() {
        DamageTypeDTO damageTypeWithEmptyName = new DamageTypeDTO();
        damageTypeWithEmptyName.setId(1);
        damageTypeWithEmptyName.setName("");
        damageTypeWithEmptyName.setDescription("A fictional damageType.");

        DamageTypeDTO damageTypeWithNullName = new DamageTypeDTO();
        damageTypeWithNullName.setId(1);
        damageTypeWithNullName.setName(null);
        damageTypeWithNullName.setDescription("A fictional damageType.");

        assertThrows(IllegalArgumentException.class, () -> damageTypeService.saveDamageType(damageTypeWithEmptyName));
        assertThrows(IllegalArgumentException.class, () -> damageTypeService.saveDamageType(damageTypeWithNullName));
    }

    @Test
    public void whenDamageTypeNameAlreadyExists_saveDamageType_ThrowsDataIntegrityViolationException() {
        when(damageTypeRepository.findByName(dto.getName())).thenReturn(Optional.of(entity));
        assertThrows(DataIntegrityViolationException.class, () -> damageTypeService.saveDamageType(dto));
        verify(damageTypeRepository, times(1)).findByName(dto.getName());
        verify(damageTypeRepository, never()).save(any(DamageType.class));
    }

    @Test
    void whenDamageTypeIdExists_deleteDamageType_DeletesTheDamageType() {
        when(damageTypeRepository.existsById(1)).thenReturn(true);
        damageTypeService.deleteDamageType(1);
        verify(damageTypeRepository, times(1)).deleteById(1);
    }

    @Test
    void whenDamageTypeIdDoesNotExist_deleteDamageType_ThrowsIllegalArgumentException() {
        when(damageTypeRepository.existsById(999)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> damageTypeService.deleteDamageType(999));
    }

    @Test
    void whenDeleteDamageTypeFails_deleteDamageType_ThrowsException() {
        when(damageTypeRepository.existsById(1)).thenReturn(true);
        doThrow(new RuntimeException("Database error")).when(damageTypeRepository).deleteById(1);

        assertThrows(RuntimeException.class, () -> damageTypeService.deleteDamageType(1));
    }

    @Test
    void whenDamageTypeIdIsFound_updateDamageType_UpdatesTheDamageType() {
        DamageTypeDTO updateDTO = new DamageTypeDTO();
        updateDTO.setName("Updated Name");

        when(damageTypeRepository.findById(1)).thenReturn(Optional.of(entity));
        when(damageTypeRepository.existsById(1)).thenReturn(true);
        when(damageTypeRepository.save(entity)).thenReturn(entity);
        when(damageTypeMapper.mapToDamageTypeDto(entity)).thenReturn(updateDTO);

        Optional<DamageTypeDTO> result = damageTypeService.updateDamageType(1, updateDTO);

        assertTrue(result.isPresent());
        assertEquals("Updated Name", result.get().getName());
    }

    @Test
    void whenDamageTypeIdIsNotFound_updateDamageType_ReturnsEmptyOptional() {
        DamageTypeDTO updateDTO = new DamageTypeDTO();
        updateDTO.setName("Updated Name");

        when(damageTypeRepository.findById(999)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> damageTypeService.updateDamageType(999, updateDTO));
    }

    @Test
    public void whenDamageTypeNameIsInvalid_updateDamageType_ThrowsIllegalArgumentException() {
        DamageTypeDTO updateEmptyName = new DamageTypeDTO();
        updateEmptyName.setName("");

        DamageTypeDTO updateNullName = new DamageTypeDTO();
        updateNullName.setName(null);

        when(damageTypeRepository.existsById(1)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> damageTypeService.updateDamageType(1, updateEmptyName));
        assertThrows(IllegalArgumentException.class, () -> damageTypeService.updateDamageType(1, updateNullName));
    }

    @Test
    public void whenDamageTypeNameAlreadyExists_updateDamageType_ThrowsDataIntegrityViolationException() {
        when(damageTypeRepository.findByName(dto.getName())).thenReturn(Optional.of(entity));

        assertThrows(IllegalArgumentException.class, () -> damageTypeService.updateDamageType(entity.getId(), dto));
    }
}
