package com.mcommings.campaigner.services;

import com.mcommings.campaigner.models.Terrain;
import com.mcommings.campaigner.models.repositories.ITerrainRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
public class TerrainTest {

    @Mock
    private ITerrainRepository terrainRepository;

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
}
