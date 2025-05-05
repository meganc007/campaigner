package com.mcommings.campaigner.services.common;

import com.mcommings.campaigner.modules.common.dtos.GovernmentDTO;
import com.mcommings.campaigner.modules.common.entities.Government;
import com.mcommings.campaigner.modules.common.mappers.GovernmentMapper;
import com.mcommings.campaigner.modules.common.repositories.IGovernmentRepository;
import com.mcommings.campaigner.modules.common.services.GovernmentService;
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
public class GovernmentTest {

    @Mock
    private GovernmentMapper governmentMapper;

    @Mock
    private IGovernmentRepository governmentRepository;

    @InjectMocks
    private GovernmentService governmentService;

    private Government entity;
    private GovernmentDTO dto;

    @BeforeEach
    void setUp() {
        entity = new Government();
        entity.setId(1);
        entity.setName("Test Government");
        entity.setDescription("A fictional land.");

        dto = new GovernmentDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());

        when(governmentMapper.mapToGovernmentDto(entity)).thenReturn(dto);
        when(governmentMapper.mapFromGovernmentDto(dto)).thenReturn(entity);
    }

    @Test
    public void whenThereAreGovernments_getGovernments_ReturnsGovernments() {
        when(governmentRepository.findAll()).thenReturn(List.of(entity));
        List<GovernmentDTO> result = governmentService.getGovernments();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Government", result.get(0).getName());
    }

    @Test
    public void whenThereAreNoGovernments_getGovernments_ReturnsEmptyList() {
        when(governmentRepository.findAll()).thenReturn(Collections.emptyList());

        List<GovernmentDTO> result = governmentService.getGovernments();

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected an empty list when there are no governments.");
    }

    @Test
    void getGovernment_ReturnsGovernmentById() {
        when(governmentRepository.findById(1)).thenReturn(Optional.of(entity));

        Optional<GovernmentDTO> result = governmentService.getGovernment(1);

        assertTrue(result.isPresent());
        assertEquals("Test Government", result.get().getName());
    }

    @Test
    void whenThereIsNotAGovernment_getGovernment_ReturnsNothing() {
        when(governmentRepository.findById(999)).thenReturn(Optional.empty());

        Optional<GovernmentDTO> result = governmentService.getGovernment(999);

        assertTrue(result.isEmpty(), "Expected empty Optional when government is not found.");
    }

    @Test
    void whenGovernmentIsValid_saveGovernment_SavesTheGovernment() {
        when(governmentRepository.save(entity)).thenReturn(entity);

        governmentService.saveGovernment(dto);

        verify(governmentRepository, times(1)).save(entity);
    }

    @Test
    public void whenGovernmentNameIsInvalid_saveGovernment_ThrowsIllegalArgumentException() {
        GovernmentDTO governmentWithEmptyName = new GovernmentDTO();
        governmentWithEmptyName.setId(1);
        governmentWithEmptyName.setName("");
        governmentWithEmptyName.setDescription("A fictional government.");

        GovernmentDTO governmentWithNullName = new GovernmentDTO();
        governmentWithNullName.setId(1);
        governmentWithNullName.setName(null);
        governmentWithNullName.setDescription("A fictional government.");

        assertThrows(IllegalArgumentException.class, () -> governmentService.saveGovernment(governmentWithEmptyName));
        assertThrows(IllegalArgumentException.class, () -> governmentService.saveGovernment(governmentWithNullName));
    }

    @Test
    public void whenGovernmentNameAlreadyExists_saveGovernment_ThrowsDataIntegrityViolationException() {
        when(governmentRepository.findByName(dto.getName())).thenReturn(Optional.of(entity));
        assertThrows(DataIntegrityViolationException.class, () -> governmentService.saveGovernment(dto));
        verify(governmentRepository, times(1)).findByName(dto.getName());
        verify(governmentRepository, never()).save(any(Government.class));
    }

    @Test
    void whenGovernmentIdExists_deleteGovernment_DeletesTheGovernment() {
        when(governmentRepository.existsById(1)).thenReturn(true);
        governmentService.deleteGovernment(1);
        verify(governmentRepository, times(1)).deleteById(1);
    }

    @Test
    void whenGovernmentIdDoesNotExist_deleteGovernment_ThrowsIllegalArgumentException() {
        when(governmentRepository.existsById(999)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> governmentService.deleteGovernment(999));
    }

    @Test
    void whenDeleteGovernmentFails_deleteGovernment_ThrowsException() {
        when(governmentRepository.existsById(1)).thenReturn(true);
        doThrow(new RuntimeException("Database error")).when(governmentRepository).deleteById(1);

        assertThrows(RuntimeException.class, () -> governmentService.deleteGovernment(1));
    }

    @Test
    void whenGovernmentIdIsFound_updateGovernment_UpdatesTheGovernment() {
        GovernmentDTO updateDTO = new GovernmentDTO();
        updateDTO.setName("Updated Name");

        when(governmentRepository.findById(1)).thenReturn(Optional.of(entity));
        when(governmentRepository.existsById(1)).thenReturn(true);
        when(governmentRepository.save(entity)).thenReturn(entity);
        when(governmentMapper.mapToGovernmentDto(entity)).thenReturn(updateDTO);

        Optional<GovernmentDTO> result = governmentService.updateGovernment(1, updateDTO);

        assertTrue(result.isPresent());
        assertEquals("Updated Name", result.get().getName());
    }

    @Test
    void whenGovernmentIdIsNotFound_updateGovernment_ReturnsEmptyOptional() {
        GovernmentDTO updateDTO = new GovernmentDTO();
        updateDTO.setName("Updated Name");

        when(governmentRepository.findById(999)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> governmentService.updateGovernment(999, updateDTO));
    }

    @Test
    public void whenGovernmentNameIsInvalid_updateGovernment_ThrowsIllegalArgumentException() {
        GovernmentDTO updateEmptyName = new GovernmentDTO();
        updateEmptyName.setName("");

        GovernmentDTO updateNullName = new GovernmentDTO();
        updateNullName.setName(null);

        when(governmentRepository.existsById(1)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> governmentService.updateGovernment(1, updateEmptyName));
        assertThrows(IllegalArgumentException.class, () -> governmentService.updateGovernment(1, updateNullName));
    }

    @Test
    public void whenGovernmentNameAlreadyExists_updateGovernment_ThrowsDataIntegrityViolationException() {
        when(governmentRepository.findByName(dto.getName())).thenReturn(Optional.of(entity));

        assertThrows(IllegalArgumentException.class, () -> governmentService.updateGovernment(entity.getId(), dto));
    }
}
