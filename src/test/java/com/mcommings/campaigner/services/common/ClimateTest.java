package com.mcommings.campaigner.services.common;

import com.mcommings.campaigner.modules.common.dtos.ClimateDTO;
import com.mcommings.campaigner.modules.common.entities.Climate;
import com.mcommings.campaigner.modules.common.mappers.ClimateMapper;
import com.mcommings.campaigner.modules.common.repositories.IClimateRepository;
import com.mcommings.campaigner.modules.common.services.ClimateService;
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
public class ClimateTest {

    @Mock
    private ClimateMapper climateMapper;

    @Mock
    private IClimateRepository climateRepository;

    @InjectMocks
    private ClimateService climateService;

    private Climate entity;
    private ClimateDTO dto;

    @BeforeEach
    void setUp() {
        entity = new Climate();
        entity.setId(1);
        entity.setName("Test Climate");
        entity.setDescription("A fictional land.");

        dto = new ClimateDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());

        when(climateMapper.mapToClimateDto(entity)).thenReturn(dto);
        when(climateMapper.mapFromClimateDto(dto)).thenReturn(entity);
    }

    @Test
    public void whenThereAreClimates_getClimates_ReturnsClimates() {
        when(climateRepository.findAll()).thenReturn(List.of(entity));
        List<ClimateDTO> result = climateService.getClimates();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Climate", result.get(0).getName());
    }

    @Test
    public void whenThereAreNoClimates_getClimates_ReturnsEmptyList() {
        when(climateRepository.findAll()).thenReturn(Collections.emptyList());

        List<ClimateDTO> result = climateService.getClimates();

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected an empty list when there are no climates.");
    }

    @Test
    void getClimate_ReturnsClimateById() {
        when(climateRepository.findById(1)).thenReturn(Optional.of(entity));

        Optional<ClimateDTO> result = climateService.getClimate(1);

        assertTrue(result.isPresent());
        assertEquals("Test Climate", result.get().getName());
    }

    @Test
    void whenThereIsNotAClimate_getClimate_ReturnsNothing() {
        when(climateRepository.findById(999)).thenReturn(Optional.empty());

        Optional<ClimateDTO> result = climateService.getClimate(999);

        assertTrue(result.isEmpty(), "Expected empty Optional when climate is not found.");
    }

    @Test
    void whenClimateIsValid_saveClimate_SavesTheClimate() {
        when(climateRepository.save(entity)).thenReturn(entity);

        climateService.saveClimate(dto);

        verify(climateRepository, times(1)).save(entity);
    }

    @Test
    public void whenClimateNameIsInvalid_saveClimate_ThrowsIllegalArgumentException() {
        ClimateDTO climateWithEmptyName = new ClimateDTO();
        climateWithEmptyName.setId(1);
        climateWithEmptyName.setName("");
        climateWithEmptyName.setDescription("A fictional climate.");

        ClimateDTO climateWithNullName = new ClimateDTO();
        climateWithNullName.setId(1);
        climateWithNullName.setName(null);
        climateWithNullName.setDescription("A fictional climate.");

        assertThrows(IllegalArgumentException.class, () -> climateService.saveClimate(climateWithEmptyName));
        assertThrows(IllegalArgumentException.class, () -> climateService.saveClimate(climateWithNullName));
    }

    @Test
    public void whenClimateNameAlreadyExists_saveClimate_ThrowsDataIntegrityViolationException() {
        when(climateRepository.findByName(dto.getName())).thenReturn(Optional.of(entity));
        assertThrows(DataIntegrityViolationException.class, () -> climateService.saveClimate(dto));
        verify(climateRepository, times(1)).findByName(dto.getName());
        verify(climateRepository, never()).save(any(Climate.class));
    }

    @Test
    void whenClimateIdExists_deleteClimate_DeletesTheClimate() {
        when(climateRepository.existsById(1)).thenReturn(true);
        climateService.deleteClimate(1);
        verify(climateRepository, times(1)).deleteById(1);
    }

    @Test
    void whenClimateIdDoesNotExist_deleteClimate_ThrowsIllegalArgumentException() {
        when(climateRepository.existsById(999)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> climateService.deleteClimate(999));
    }

    @Test
    void whenDeleteClimateFails_deleteClimate_ThrowsException() {
        when(climateRepository.existsById(1)).thenReturn(true);
        doThrow(new RuntimeException("Database error")).when(climateRepository).deleteById(1);

        assertThrows(RuntimeException.class, () -> climateService.deleteClimate(1));
    }

    @Test
    void whenClimateIdIsFound_updateClimate_UpdatesTheClimate() {
        ClimateDTO updateDTO = new ClimateDTO();
        updateDTO.setName("Updated Name");

        when(climateRepository.findById(1)).thenReturn(Optional.of(entity));
        when(climateRepository.existsById(1)).thenReturn(true);
        when(climateRepository.save(entity)).thenReturn(entity);
        when(climateMapper.mapToClimateDto(entity)).thenReturn(updateDTO);

        Optional<ClimateDTO> result = climateService.updateClimate(1, updateDTO);

        assertTrue(result.isPresent());
        assertEquals("Updated Name", result.get().getName());
    }

    @Test
    void whenClimateIdIsNotFound_updateClimate_ReturnsEmptyOptional() {
        ClimateDTO updateDTO = new ClimateDTO();
        updateDTO.setName("Updated Name");

        when(climateRepository.findById(999)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> climateService.updateClimate(999, updateDTO));
    }

    @Test
    public void whenClimateNameIsInvalid_updateClimate_ThrowsIllegalArgumentException() {
        ClimateDTO updateEmptyName = new ClimateDTO();
        updateEmptyName.setName("");

        ClimateDTO updateNullName = new ClimateDTO();
        updateNullName.setName(null);

        when(climateRepository.existsById(1)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> climateService.updateClimate(1, updateEmptyName));
        assertThrows(IllegalArgumentException.class, () -> climateService.updateClimate(1, updateNullName));
    }

    @Test
    public void whenClimateNameAlreadyExists_updateClimate_ThrowsDataIntegrityViolationException() {
        when(climateRepository.findByName(dto.getName())).thenReturn(Optional.of(entity));

        assertThrows(IllegalArgumentException.class, () -> climateService.updateClimate(entity.getId(), dto));
    }
}
