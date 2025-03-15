package com.mcommings.campaigner.services.locations;

import com.mcommings.campaigner.modules.locations.dtos.PlaceTypeDTO;
import com.mcommings.campaigner.modules.locations.entities.PlaceType;
import com.mcommings.campaigner.modules.locations.mappers.PlaceTypeMapper;
import com.mcommings.campaigner.modules.locations.repositories.IPlaceTypesRepository;
import com.mcommings.campaigner.modules.locations.services.PlaceTypeService;
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
public class PlaceTypeTest {

    @Mock
    private PlaceTypeMapper placeTypeMapper;

    @Mock
    private IPlaceTypesRepository placeTypeRepository;

    @InjectMocks
    private PlaceTypeService placeTypeService;

    private PlaceType entity;
    private PlaceTypeDTO dto;

    @BeforeEach
    void setUp() {
        entity = new PlaceType();
        entity.setId(1);
        entity.setName("Test Place Type");
        entity.setDescription("This is a type of place.");

        dto = new PlaceTypeDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());

        // Mocking the mapper behavior
        when(placeTypeMapper.mapToPlaceTypeDto(entity)).thenReturn(dto);
        when(placeTypeMapper.mapFromPlaceTypeDto(dto)).thenReturn(entity);
    }

    @Test
    public void whenThereArePlaceTypes_getPlaceTypes_ReturnsPlaceTypes() {
        when(placeTypeRepository.findAll()).thenReturn(List.of(entity));
        List<PlaceTypeDTO> result = placeTypeService.getPlaceTypes();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Place Type", result.get(0).getName());
    }

    @Test
    public void whenThereAreNoPlaceTypes_getPlaceTypes_ReturnsEmptyList() {
        when(placeTypeRepository.findAll()).thenReturn(Collections.emptyList());

        List<PlaceTypeDTO> result = placeTypeService.getPlaceTypes();

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected an empty list when there are no placeTypes.");
    }

    @Test
    void getPlaceType_ReturnsPlaceTypeById() {
        when(placeTypeRepository.findById(1)).thenReturn(Optional.of(entity));

        Optional<PlaceTypeDTO> result = placeTypeService.getPlaceType(1);

        assertTrue(result.isPresent());
        assertEquals("Test Place Type", result.get().getName());
    }

    @Test
    void whenThereIsNotAPlaceType_getPlaceType_ReturnsNothing() {
        when(placeTypeRepository.findById(999)).thenReturn(Optional.empty());

        Optional<PlaceTypeDTO> result = placeTypeService.getPlaceType(999);

        assertTrue(result.isEmpty(), "Expected empty Optional when placeType is not found.");
    }

    @Test
    void whenPlaceTypeIsValid_savePlaceType_SavesThePlaceType() {
        when(placeTypeRepository.save(entity)).thenReturn(entity);

        placeTypeService.savePlaceType(dto);

        verify(placeTypeRepository, times(1)).save(entity);
    }

    @Test
    public void whenPlaceTypeNameIsInvalid_savePlaceType_ThrowsIllegalArgumentException() {
        PlaceTypeDTO placeTypeWithEmptyName = new PlaceTypeDTO();
        placeTypeWithEmptyName.setId(1);
        placeTypeWithEmptyName.setName("");
        placeTypeWithEmptyName.setDescription("A placeType.");

        PlaceTypeDTO placeTypeWithNullName = new PlaceTypeDTO();
        placeTypeWithNullName.setId(1);
        placeTypeWithNullName.setName(null);
        placeTypeWithNullName.setDescription("A placeType.");

        assertThrows(IllegalArgumentException.class, () -> placeTypeService.savePlaceType(placeTypeWithEmptyName));
        assertThrows(IllegalArgumentException.class, () -> placeTypeService.savePlaceType(placeTypeWithNullName));
    }

    @Test
    public void whenPlaceTypeNameAlreadyExists_savePlaceType_ThrowsDataIntegrityViolationException() {
        when(placeTypeRepository.findByName(dto.getName())).thenReturn(Optional.of(entity));
        assertThrows(DataIntegrityViolationException.class, () -> placeTypeService.savePlaceType(dto));
        verify(placeTypeRepository, times(1)).findByName(dto.getName());
        verify(placeTypeRepository, never()).save(any(PlaceType.class));
    }

    @Test
    void whenPlaceTypeIdExists_deletePlaceType_DeletesThePlaceType() {
        when(placeTypeRepository.existsById(1)).thenReturn(true);
        placeTypeService.deletePlaceType(1);
        verify(placeTypeRepository, times(1)).deleteById(1);
    }

    @Test
    void whenPlaceTypeIdDoesNotExist_deletePlaceType_ThrowsIllegalArgumentException() {
        when(placeTypeRepository.existsById(999)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> placeTypeService.deletePlaceType(999));
    }

    @Test
    void whenDeletePlaceTypeFails_deletePlaceType_ThrowsException() {
        when(placeTypeRepository.existsById(1)).thenReturn(true);
        doThrow(new RuntimeException("Database error")).when(placeTypeRepository).deleteById(1);

        assertThrows(RuntimeException.class, () -> placeTypeService.deletePlaceType(1));
    }

    @Test
    void whenPlaceTypeIdIsFound_updatePlaceType_UpdatesThePlaceType() {
        PlaceTypeDTO updateDTO = new PlaceTypeDTO();
        updateDTO.setName("Updated Name");

        when(placeTypeRepository.findById(1)).thenReturn(Optional.of(entity));
        when(placeTypeRepository.existsById(1)).thenReturn(true);
        when(placeTypeRepository.save(entity)).thenReturn(entity);
        when(placeTypeMapper.mapToPlaceTypeDto(entity)).thenReturn(updateDTO);

        Optional<PlaceTypeDTO> result = placeTypeService.updatePlaceType(1, updateDTO);

        assertTrue(result.isPresent());
        assertEquals("Updated Name", result.get().getName());
    }

    @Test
    void whenPlaceTypeIdIsNotFound_updatePlaceType_ReturnsEmptyOptional() {
        PlaceTypeDTO updateDTO = new PlaceTypeDTO();
        updateDTO.setName("Updated Name");

        when(placeTypeRepository.findById(999)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> placeTypeService.updatePlaceType(999, updateDTO));
    }

    @Test
    public void whenPlaceTypeNameIsInvalid_updatePlaceType_ThrowsIllegalArgumentException() {
        PlaceTypeDTO updateEmptyName = new PlaceTypeDTO();
        updateEmptyName.setName("");

        PlaceTypeDTO updateNullName = new PlaceTypeDTO();
        updateNullName.setName(null);

        when(placeTypeRepository.existsById(1)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> placeTypeService.updatePlaceType(1, updateEmptyName));
        assertThrows(IllegalArgumentException.class, () -> placeTypeService.updatePlaceType(1, updateNullName));
    }

    @Test
    public void whenPlaceTypeNameAlreadyExists_updatePlaceType_ThrowsDataIntegrityViolationException() {
        when(placeTypeRepository.findByName(dto.getName())).thenReturn(Optional.of(entity));

        assertThrows(IllegalArgumentException.class, () -> placeTypeService.updatePlaceType(entity.getId(), dto));
    }

}
