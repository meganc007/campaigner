package com.mcommings.campaigner.modules.locations.controllers;

import com.mcommings.campaigner.modules.locations.dtos.terrains.CreateTerrainDTO;
import com.mcommings.campaigner.modules.locations.dtos.terrains.UpdateTerrainDTO;
import com.mcommings.campaigner.modules.locations.dtos.terrains.ViewTerrainDTO;
import com.mcommings.campaigner.modules.locations.services.TerrainService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/terrains")
public class TerrainController {

    private final TerrainService terrainService;

    @GetMapping
    public List<ViewTerrainDTO> getTerrains() {

        return terrainService.getAll();
    }

    @GetMapping(path = "/{terrainId}")
    public ViewTerrainDTO getTerrain(@PathVariable int terrainId) {
        return terrainService.getById(terrainId);
    }

    @PostMapping
    public ViewTerrainDTO createTerrain(@Valid @RequestBody CreateTerrainDTO terrain) {
        return terrainService.create(terrain);
    }

    @PutMapping
    public ViewTerrainDTO updateTerrain(@Valid @RequestBody UpdateTerrainDTO terrain) {
        return terrainService.update(terrain);
    }

    @DeleteMapping(path = "/{terrainId}")
    public void deleteTerrain(@PathVariable int terrainId) {
        terrainService.delete(terrainId);
    }
}
