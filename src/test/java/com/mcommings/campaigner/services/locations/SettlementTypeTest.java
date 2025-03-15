package com.mcommings.campaigner.services.locations;

import com.mcommings.campaigner.modules.locations.dtos.SettlementTypeDTO;
import com.mcommings.campaigner.modules.locations.entities.SettlementType;
import com.mcommings.campaigner.modules.locations.mappers.SettlementTypeMapper;
import com.mcommings.campaigner.modules.locations.repositories.ISettlementTypeRepository;
import com.mcommings.campaigner.modules.locations.services.SettlementTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class SettlementTypeTest {

    @Mock
    private SettlementTypeMapper settlementTypeMapper;

    @Mock
    private ISettlementTypeRepository settlementTypeRepository;

    @InjectMocks
    private SettlementTypeService settlementTypeService;

    private SettlementType entity;
    private SettlementTypeDTO dto;

    @BeforeEach
    void setUp() {
        entity = new SettlementType();
        entity.setId(1);
        entity.setName("Test Place Type");
        entity.setDescription("This is a type of place.");

        dto = new SettlementTypeDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());

        // Mocking the mapper behavior
        when(settlementTypeMapper.mapToSettlementTypeDto(entity)).thenReturn(dto);
        when(settlementTypeMapper.mapFromSettlementTypeDto(dto)).thenReturn(entity);
    }

    @Test
    public void whenThereAreSettlementTypes_getSettlementTypes_ReturnsSettlementTypes() {
        when(settlementTypeRepository.findAll()).thenReturn(List.of(entity));
        List<SettlementTypeDTO> result = settlementTypeService.getSettlementTypes();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Place Type", result.get(0).getName());
    }

    @Test
    public void whenThereAreNoSettlementTypes_getSettlementTypes_ReturnsEmptyList() {
        when(settlementTypeRepository.findAll()).thenReturn(Collections.emptyList());

        List<SettlementTypeDTO> result = settlementTypeService.getSettlementTypes();

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected an empty list when there are no settlementTypes.");
    }

    @Test
    void getSettlementType_ReturnsSettlementTypeById() {
        when(settlementTypeRepository.findById(1)).thenReturn(Optional.of(entity));

        Optional<SettlementTypeDTO> result = settlementTypeService.getSettlementType(1);

        assertTrue(result.isPresent());
        assertEquals("Test Place Type", result.get().getName());
    }

    @Test
    void whenThereIsNotASettlementType_getSettlementType_ReturnsNothing() {
        when(settlementTypeRepository.findById(999)).thenReturn(Optional.empty());

        Optional<SettlementTypeDTO> result = settlementTypeService.getSettlementType(999);

        assertTrue(result.isEmpty(), "Expected empty Optional when settlementType is not found.");
    }

    @Test
    void whenSettlementTypeIsValid_saveSettlementType_SavesTheSettlementType() {
        when(settlementTypeRepository.save(entity)).thenReturn(entity);

        settlementTypeService.saveSettlementType(dto);

        verify(settlementTypeRepository, times(1)).save(entity);
    }

    @Test
    public void whenSettlementTypeNameIsInvalid_saveSettlementType_ThrowsIllegalArgumentException() {
        SettlementTypeDTO settlementTypeWithEmptyName = new SettlementTypeDTO();
        settlementTypeWithEmptyName.setId(1);
        settlementTypeWithEmptyName.setName("");
        settlementTypeWithEmptyName.setDescription("A settlementType.");

        SettlementTypeDTO settlementTypeWithNullName = new SettlementTypeDTO();
        settlementTypeWithNullName.setId(1);
        settlementTypeWithNullName.setName(null);
        settlementTypeWithNullName.setDescription("A settlementType.");

        assertThrows(IllegalArgumentException.class, () -> settlementTypeService.saveSettlementType(settlementTypeWithEmptyName));
        assertThrows(IllegalArgumentException.class, () -> settlementTypeService.saveSettlementType(settlementTypeWithNullName));
    }

    @Test
    public void whenSettlementTypeNameAlreadyExists_saveSettlementType_ThrowsDataIntegrityViolationException() {
        when(settlementTypeRepository.findByName(dto.getName())).thenReturn(Optional.of(entity));
        assertThrows(DataIntegrityViolationException.class, () -> settlementTypeService.saveSettlementType(dto));
        verify(settlementTypeRepository, times(1)).findByName(dto.getName());
        verify(settlementTypeRepository, never()).save(any(SettlementType.class));
    }

    @Test
    void whenSettlementTypeIdExists_deleteSettlementType_DeletesTheSettlementType() {
        when(settlementTypeRepository.existsById(1)).thenReturn(true);
        settlementTypeService.deleteSettlementType(1);
        verify(settlementTypeRepository, times(1)).deleteById(1);
    }

    @Test
    void whenSettlementTypeIdDoesNotExist_deleteSettlementType_ThrowsIllegalArgumentException() {
        when(settlementTypeRepository.existsById(999)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> settlementTypeService.deleteSettlementType(999));
    }

    @Test
    void whenDeleteSettlementTypeFails_deleteSettlementType_ThrowsException() {
        when(settlementTypeRepository.existsById(1)).thenReturn(true);
        doThrow(new RuntimeException("Database error")).when(settlementTypeRepository).deleteById(1);

        assertThrows(RuntimeException.class, () -> settlementTypeService.deleteSettlementType(1));
    }

    @Test
    void whenSettlementTypeIdIsFound_updateSettlementType_UpdatesTheSettlementType() {
        SettlementTypeDTO updateDTO = new SettlementTypeDTO();
        updateDTO.setName("Updated Name");

        when(settlementTypeRepository.findById(1)).thenReturn(Optional.of(entity));
        when(settlementTypeRepository.existsById(1)).thenReturn(true);
        when(settlementTypeRepository.save(entity)).thenReturn(entity);
        when(settlementTypeMapper.mapToSettlementTypeDto(entity)).thenReturn(updateDTO);

        Optional<SettlementTypeDTO> result = settlementTypeService.updateSettlementType(1, updateDTO);

        assertTrue(result.isPresent());
        assertEquals("Updated Name", result.get().getName());
    }

    @Test
    void whenSettlementTypeIdIsNotFound_updateSettlementType_ReturnsEmptyOptional() {
        SettlementTypeDTO updateDTO = new SettlementTypeDTO();
        updateDTO.setName("Updated Name");

        when(settlementTypeRepository.findById(999)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> settlementTypeService.updateSettlementType(999, updateDTO));
    }

    @Test
    public void whenSettlementTypeNameIsInvalid_updateSettlementType_ThrowsIllegalArgumentException() {
        SettlementTypeDTO updateEmptyName = new SettlementTypeDTO();
        updateEmptyName.setName("");

        SettlementTypeDTO updateNullName = new SettlementTypeDTO();
        updateNullName.setName(null);

        when(settlementTypeRepository.existsById(1)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> settlementTypeService.updateSettlementType(1, updateEmptyName));
        assertThrows(IllegalArgumentException.class, () -> settlementTypeService.updateSettlementType(1, updateNullName));
    }

    @Test
    public void whenSettlementTypeNameAlreadyExists_updateSettlementType_ThrowsDataIntegrityViolationException() {
        when(settlementTypeRepository.findByName(dto.getName())).thenReturn(Optional.of(entity));

        assertThrows(IllegalArgumentException.class, () -> settlementTypeService.updateSettlementType(entity.getId(), dto));
    }
}
