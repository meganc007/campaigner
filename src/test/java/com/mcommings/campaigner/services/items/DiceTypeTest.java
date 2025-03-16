package com.mcommings.campaigner.services.items;

import com.mcommings.campaigner.modules.items.dtos.DiceTypeDTO;
import com.mcommings.campaigner.modules.items.entities.DiceType;
import com.mcommings.campaigner.modules.items.mappers.DiceTypeMapper;
import com.mcommings.campaigner.modules.items.repositories.IDiceTypeRepository;
import com.mcommings.campaigner.modules.items.services.DiceTypeService;
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
public class DiceTypeTest {

    @Mock
    private DiceTypeMapper diceTypeMapper;

    @Mock
    private IDiceTypeRepository diceTypeRepository;

    @InjectMocks
    private DiceTypeService diceTypeService;

    private DiceType entity;
    private DiceTypeDTO dto;

    @BeforeEach
    void setUp() {
        Random random = new Random();
        entity = new DiceType();
        entity.setId(1);
        entity.setName("Test DiceType");
        entity.setDescription("A fictional diceType.");
        entity.setMax_roll(random.nextInt(100) + 1);

        dto = new DiceTypeDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setMax_roll(entity.getMax_roll());

        when(diceTypeMapper.mapToDiceTypeDto(entity)).thenReturn(dto);
        when(diceTypeMapper.mapFromDiceTypeDto(dto)).thenReturn(entity);
    }

    @Test
    public void whenThereAreDiceTypes_getDiceTypes_ReturnsDiceTypes() {
        when(diceTypeRepository.findAll()).thenReturn(List.of(entity));
        List<DiceTypeDTO> result = diceTypeService.getDiceTypes();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test DiceType", result.get(0).getName());
    }

    @Test
    public void whenThereAreNoDiceTypes_getDiceTypes_ReturnsEmptyList() {
        when(diceTypeRepository.findAll()).thenReturn(Collections.emptyList());

        List<DiceTypeDTO> result = diceTypeService.getDiceTypes();

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected an empty list when there are no diceTypes.");
    }

    @Test
    void whenThereIsADiceType_getDiceType_ReturnsDiceTypeById() {
        when(diceTypeRepository.findById(1)).thenReturn(Optional.of(entity));

        Optional<DiceTypeDTO> result = diceTypeService.getDiceType(1);

        assertTrue(result.isPresent());
        assertEquals("Test DiceType", result.get().getName());
    }

    @Test
    void whenThereIsNotADiceType_getDiceType_ReturnsNothing() {
        when(diceTypeRepository.findById(999)).thenReturn(Optional.empty());

        Optional<DiceTypeDTO> result = diceTypeService.getDiceType(999);

        assertTrue(result.isEmpty(), "Expected empty Optional when diceType is not found.");
    }

    @Test
    void whenDiceTypeIsValid_saveDiceType_SavesTheDiceType() {
        when(diceTypeRepository.save(entity)).thenReturn(entity);

        diceTypeService.saveDiceType(dto);

        verify(diceTypeRepository, times(1)).save(entity);
    }

    @Test
    public void whenDiceTypeNameIsInvalid_saveDiceType_ThrowsIllegalArgumentException() {
        DiceTypeDTO diceTypeWithEmptyName = new DiceTypeDTO();
        diceTypeWithEmptyName.setId(1);
        diceTypeWithEmptyName.setName("");
        diceTypeWithEmptyName.setDescription("A fictional diceType.");
        diceTypeWithEmptyName.setMax_roll(6);

        DiceTypeDTO diceTypeWithNullName = new DiceTypeDTO();
        diceTypeWithNullName.setId(1);
        diceTypeWithNullName.setName(null);
        diceTypeWithNullName.setDescription("A fictional diceType.");
        diceTypeWithNullName.setMax_roll(6);

        assertThrows(IllegalArgumentException.class, () -> diceTypeService.saveDiceType(diceTypeWithEmptyName));
        assertThrows(IllegalArgumentException.class, () -> diceTypeService.saveDiceType(diceTypeWithNullName));
    }

    @Test
    public void whenDiceTypeNameAlreadyExists_saveDiceType_ThrowsDataIntegrityViolationException() {
        when(diceTypeRepository.findByName(dto.getName())).thenReturn(Optional.of(entity));
        assertThrows(DataIntegrityViolationException.class, () -> diceTypeService.saveDiceType(dto));
        verify(diceTypeRepository, times(1)).findByName(dto.getName());
        verify(diceTypeRepository, never()).save(any(DiceType.class));
    }

    @Test
    void whenDiceTypeIdExists_deleteDiceType_DeletesTheDiceType() {
        when(diceTypeRepository.existsById(1)).thenReturn(true);
        diceTypeService.deleteDiceType(1);
        verify(diceTypeRepository, times(1)).deleteById(1);
    }

    @Test
    void whenDiceTypeIdDoesNotExist_deleteDiceType_ThrowsIllegalArgumentException() {
        when(diceTypeRepository.existsById(999)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> diceTypeService.deleteDiceType(999));
    }

    @Test
    void whenDeleteDiceTypeFails_deleteDiceType_ThrowsException() {
        when(diceTypeRepository.existsById(1)).thenReturn(true);
        doThrow(new RuntimeException("Database error")).when(diceTypeRepository).deleteById(1);

        assertThrows(RuntimeException.class, () -> diceTypeService.deleteDiceType(1));
    }

    @Test
    void whenDiceTypeIdIsFound_updateDiceType_UpdatesTheDiceType() {
        DiceTypeDTO updateDTO = new DiceTypeDTO();
        updateDTO.setName("Updated Name");

        when(diceTypeRepository.findById(1)).thenReturn(Optional.of(entity));
        when(diceTypeRepository.existsById(1)).thenReturn(true);
        when(diceTypeRepository.save(entity)).thenReturn(entity);
        when(diceTypeMapper.mapToDiceTypeDto(entity)).thenReturn(updateDTO);

        Optional<DiceTypeDTO> result = diceTypeService.updateDiceType(1, updateDTO);

        assertTrue(result.isPresent());
        assertEquals("Updated Name", result.get().getName());
    }

    @Test
    void whenDiceTypeIdIsNotFound_updateDiceType_ReturnsEmptyOptional() {
        DiceTypeDTO updateDTO = new DiceTypeDTO();
        updateDTO.setName("Updated Name");

        when(diceTypeRepository.findById(999)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> diceTypeService.updateDiceType(999, updateDTO));
    }

    @Test
    public void whenDiceTypeNameIsInvalid_updateDiceType_ThrowsIllegalArgumentException() {
        DiceTypeDTO updateEmptyName = new DiceTypeDTO();
        updateEmptyName.setName("");

        DiceTypeDTO updateNullName = new DiceTypeDTO();
        updateNullName.setName(null);

        when(diceTypeRepository.existsById(1)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> diceTypeService.updateDiceType(1, updateEmptyName));
        assertThrows(IllegalArgumentException.class, () -> diceTypeService.updateDiceType(1, updateNullName));
    }

    @Test
    public void whenDiceTypeNameAlreadyExists_updateDiceType_ThrowsDataIntegrityViolationException() {
        when(diceTypeRepository.findByName(dto.getName())).thenReturn(Optional.of(entity));

        assertThrows(IllegalArgumentException.class, () -> diceTypeService.updateDiceType(entity.getId(), dto));
    }
}
