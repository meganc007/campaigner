package com.mcommings.campaigner.services.locations;

import com.mcommings.campaigner.modules.locations.dtos.TerrainDTO;
import com.mcommings.campaigner.modules.locations.entities.Terrain;
import com.mcommings.campaigner.modules.locations.mappers.TerrainMapper;
import com.mcommings.campaigner.modules.locations.repositories.ITerrainRepository;
import com.mcommings.campaigner.modules.locations.services.TerrainService;
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
public class TerrainTest {

    @Mock
    private TerrainMapper terrainMapper;

    @Mock
    private ITerrainRepository terrainRepository;

    @InjectMocks
    private TerrainService terrainService;

    private Terrain entity;
    private TerrainDTO dto;

    @BeforeEach
    void setUp() {
        entity = new Terrain();
        entity.setId(1);
        entity.setName("Test Terrain");
        entity.setDescription("This is a terrain.");

        dto = new TerrainDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());

        // Mocking the mapper behavior
        when(terrainMapper.mapToTerrainDto(entity)).thenReturn(dto);
        when(terrainMapper.mapFromTerrainDto(dto)).thenReturn(entity);
    }

    @Test
    public void whenThereAreTerrains_getTerrains_ReturnsTerrains() {
        when(terrainRepository.findAll()).thenReturn(List.of(entity));
        List<TerrainDTO> result = terrainService.getTerrains();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Terrain", result.get(0).getName());
    }

    @Test
    public void whenThereAreNoTerrains_getTerrains_ReturnsEmptyList() {
        when(terrainRepository.findAll()).thenReturn(Collections.emptyList());

        List<TerrainDTO> result = terrainService.getTerrains();

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Expected an empty list when there are no terrains.");
    }

    @Test
    void getTerrain_ReturnsTerrainById() {
        when(terrainRepository.findById(1)).thenReturn(Optional.of(entity));

        Optional<TerrainDTO> result = terrainService.getTerrain(1);

        assertTrue(result.isPresent());
        assertEquals("Test Terrain", result.get().getName());
    }

    @Test
    void whenThereIsNotATerrain_getTerrain_ReturnsNothing() {
        when(terrainRepository.findById(999)).thenReturn(Optional.empty());

        Optional<TerrainDTO> result = terrainService.getTerrain(999);

        assertTrue(result.isEmpty(), "Expected empty Optional when terrain is not found.");
    }

    @Test
    void whenTerrainIsValid_saveTerrain_SavesTheTerrain() {
        when(terrainRepository.save(entity)).thenReturn(entity);

        terrainService.saveTerrain(dto);

        verify(terrainRepository, times(1)).save(entity);
    }

    @Test
    public void whenTerrainNameIsInvalid_saveTerrain_ThrowsIllegalArgumentException() {
        TerrainDTO terrainWithEmptyName = new TerrainDTO();
        terrainWithEmptyName.setId(1);
        terrainWithEmptyName.setName("");
        terrainWithEmptyName.setDescription("A fictional terrain.");

        TerrainDTO terrainWithNullName = new TerrainDTO();
        terrainWithNullName.setId(1);
        terrainWithNullName.setName(null);
        terrainWithNullName.setDescription("A fictional terrain.");

        assertThrows(IllegalArgumentException.class, () -> terrainService.saveTerrain(terrainWithEmptyName));
        assertThrows(IllegalArgumentException.class, () -> terrainService.saveTerrain(terrainWithNullName));
    }

    @Test
    public void whenTerrainNameAlreadyExists_saveTerrain_ThrowsDataIntegrityViolationException() {
        when(terrainRepository.findByName(dto.getName())).thenReturn(Optional.of(entity));
        assertThrows(DataIntegrityViolationException.class, () -> terrainService.saveTerrain(dto));
        verify(terrainRepository, times(1)).findByName(dto.getName());
        verify(terrainRepository, never()).save(any(Terrain.class));
    }

    @Test
    void whenTerrainIdExists_deleteTerrain_DeletesTheTerrain() {
        when(terrainRepository.existsById(1)).thenReturn(true);
        terrainService.deleteTerrain(1);
        verify(terrainRepository, times(1)).deleteById(1);
    }

    @Test
    void whenTerrainIdDoesNotExist_deleteTerrain_ThrowsIllegalArgumentException() {
        when(terrainRepository.existsById(999)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> terrainService.deleteTerrain(999));
    }

    @Test
    void whenDeleteTerrainFails_deleteTerrain_ThrowsException() {
        when(terrainRepository.existsById(1)).thenReturn(true);
        doThrow(new RuntimeException("Database error")).when(terrainRepository).deleteById(1);

        assertThrows(RuntimeException.class, () -> terrainService.deleteTerrain(1));
    }

    @Test
    void whenTerrainIdIsFound_updateTerrain_UpdatesTheTerrain() {
        TerrainDTO updateDTO = new TerrainDTO();
        updateDTO.setName("Updated Name");

        when(terrainRepository.findById(1)).thenReturn(Optional.of(entity));
        when(terrainRepository.existsById(1)).thenReturn(true);
        when(terrainRepository.save(entity)).thenReturn(entity);
        when(terrainMapper.mapToTerrainDto(entity)).thenReturn(updateDTO);

        Optional<TerrainDTO> result = terrainService.updateTerrain(1, updateDTO);

        assertTrue(result.isPresent());
        assertEquals("Updated Name", result.get().getName());
    }

    @Test
    void whenTerrainIdIsNotFound_updateTerrain_ReturnsEmptyOptional() {
        TerrainDTO updateDTO = new TerrainDTO();
        updateDTO.setName("Updated Name");

        when(terrainRepository.findById(999)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> terrainService.updateTerrain(999, updateDTO));
    }

    @Test
    public void whenTerrainNameIsInvalid_updateTerrain_ThrowsIllegalArgumentException() {
        TerrainDTO updateEmptyName = new TerrainDTO();
        updateEmptyName.setName("");

        TerrainDTO updateNullName = new TerrainDTO();
        updateNullName.setName(null);

        when(terrainRepository.existsById(1)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> terrainService.updateTerrain(1, updateEmptyName));
        assertThrows(IllegalArgumentException.class, () -> terrainService.updateTerrain(1, updateNullName));
    }

    @Test
    public void whenTerrainNameAlreadyExists_updateTerrain_ThrowsDataIntegrityViolationException() {
        when(terrainRepository.findByName(dto.getName())).thenReturn(Optional.of(entity));

        assertThrows(IllegalArgumentException.class, () -> terrainService.updateTerrain(entity.getId(), dto));
    }

}
