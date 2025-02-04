package com.mcommings.campaigner.services.locations;

import com.mcommings.campaigner.entities.RepositoryHelper;
import com.mcommings.campaigner.entities.locations.Place;
import com.mcommings.campaigner.entities.locations.Terrain;
import com.mcommings.campaigner.repositories.locations.IPlaceRepository;
import com.mcommings.campaigner.repositories.locations.ITerrainRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;

import java.util.*;

import static com.mcommings.campaigner.enums.ForeignKey.FK_TERRAIN;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TerrainTest {

    @Mock
    private ITerrainRepository terrainRepository;
    @Mock
    private IPlaceRepository placeRepository;

    @InjectMocks
    private TerrainService terrainService;

    @Test
    public void whenThereAreTerrains_getTerrains_ReturnsTerrains() {
        List<Terrain> terrains = new ArrayList<>();
        terrains.add(new Terrain(1, "Terrain 1", "Description 1"));
        terrains.add(new Terrain(2, "Terrain 2", "Description 2"));
        when(terrainRepository.findAll()).thenReturn(terrains);

        List<Terrain> result = terrainService.getTerrains();

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(terrains, result);
    }

    @Test
    public void whenThereAreNoTerrains_getTerrains_ReturnsNothing() {
        List<Terrain> terrains = new ArrayList<>();
        when(terrainRepository.findAll()).thenReturn(terrains);

        List<Terrain> result = terrainService.getTerrains();

        Assertions.assertEquals(0, result.size());
        Assertions.assertEquals(terrains, result);
    }

    @Test
    public void whenTerrainIsValid_saveTerrain_SavesTheTerrain() {
        Terrain terrain = new Terrain(1, "Terrain 1", "Description 1");
        when(terrainRepository.saveAndFlush(terrain)).thenReturn(terrain);

        assertDoesNotThrow(() -> terrainService.saveTerrain(terrain));
        verify(terrainRepository, times(1)).saveAndFlush(terrain);
    }

    @Test
    public void whenTerrainNameIsInvalid_saveTerrain_ThrowsIllegalArgumentException() {
        Terrain terrainWithEmptyName = new Terrain(1, "", "Description 1");
        Terrain terrainWithNullName = new Terrain(2, null, "Description 2");

        assertThrows(IllegalArgumentException.class, () -> terrainService.saveTerrain(terrainWithEmptyName));
        assertThrows(IllegalArgumentException.class, () -> terrainService.saveTerrain(terrainWithNullName));
    }

    @Test
    public void whenTerrainNameAlreadyExists_saveTerrain_ThrowsDataIntegrityViolationException() {
        Terrain terrain = new Terrain(1, "Terrain 1", "Description 1");
        Terrain terrainWithDuplicatedName = new Terrain(2, "Terrain 1", "Description 2");
        when(terrainRepository.saveAndFlush(terrain)).thenReturn(terrain);
        when(terrainRepository.saveAndFlush(terrainWithDuplicatedName)).thenThrow(DataIntegrityViolationException.class);

        assertDoesNotThrow(() -> terrainService.saveTerrain(terrain));
        assertThrows(DataIntegrityViolationException.class, () -> terrainService.saveTerrain(terrainWithDuplicatedName));
    }

    @Test
    public void whenTerrainIdExists_deleteTerrain_DeletesTheTerrain() {
        int terrainId = 1;
        when(terrainRepository.existsById(terrainId)).thenReturn(true);
        assertDoesNotThrow(() -> terrainService.deleteTerrain(terrainId));
        verify(terrainRepository, times(1)).deleteById(terrainId);
    }

    @Test
    public void whenTerrainIdDoesNotExist_deleteTerrain_ThrowsIllegalArgumentException() {
        int terrainId = 9000;
        when(terrainRepository.existsById(terrainId)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> terrainService.deleteTerrain(terrainId));
    }

    @Test
    public void whenTerrainIdIsAForeignKey_deleteTerrain_ThrowsDataIntegrityViolationException() {
        int terrainId = 1;
        Place place = new Place(1, "Place", "Description", UUID.randomUUID(), 1, terrainId, 1, 1, 1);
        List<CrudRepository> repositories = new ArrayList<>(Arrays.asList(placeRepository));
        List<Place> places = new ArrayList<>(Arrays.asList(place));

        when(terrainRepository.existsById(terrainId)).thenReturn(true);
        when(placeRepository.findByfk_terrain(terrainId)).thenReturn(places);

        boolean actual = RepositoryHelper.isForeignKey(repositories, FK_TERRAIN.columnName, terrainId);
        Assertions.assertTrue(actual);
        assertThrows(DataIntegrityViolationException.class, () -> terrainService.deleteTerrain(terrainId));
    }

    @Test
    public void whenTerrainIdIsFound_updateTerrain_UpdatesTheTerrain() {
        int terrainId = 1;
        Terrain terrain = new Terrain(terrainId, "Old Terrain Name", "Old Description");
        Terrain terrainToUpdate = new Terrain(terrainId, "Updated Terrain Name", "Updated Description");

        when(terrainRepository.existsById(terrainId)).thenReturn(true);
        when(terrainRepository.findById(terrainId)).thenReturn(Optional.of(terrain));

        terrainService.updateTerrain(terrainId, terrainToUpdate);

        verify(terrainRepository).findById(terrainId);

        Terrain result = terrainRepository.findById(terrainId).get();
        Assertions.assertEquals(terrainToUpdate.getName(), result.getName());
        Assertions.assertEquals(terrainToUpdate.getDescription(), result.getDescription());
    }

    @Test
    public void whenTerrainIdIsNotFound_updateTerrain_ThrowsIllegalArgumentException() {
        int terrainId = 1;
        Terrain terrain = new Terrain(terrainId, "Old Terrain Name", "Old Description");

        when(terrainRepository.existsById(terrainId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> terrainService.updateTerrain(terrainId, terrain));
    }

    @Test
    public void whenSomeTerrainFieldsChanged_updateTerrain_OnlyUpdatesChangedFields() {
        int terrainId = 1;
        Terrain terrain = new Terrain(terrainId, "Name", "Old Terrain Description");

        String newDescription = "New Terrain description";

        Terrain terrainToUpdate = new Terrain();
        terrainToUpdate.setDescription(newDescription);

        when(terrainRepository.existsById(terrainId)).thenReturn(true);
        when(terrainRepository.findById(terrainId)).thenReturn(Optional.of(terrain));

        terrainService.updateTerrain(terrainId, terrainToUpdate);

        verify(terrainRepository).findById(terrainId);

        Terrain result = terrainRepository.findById(terrainId).get();
        Assertions.assertEquals(terrain.getName(), result.getName());
        Assertions.assertEquals(newDescription, result.getDescription());
    }
}
