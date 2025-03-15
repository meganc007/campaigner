package com.mcommings.campaigner.locations.controllers;

import com.mcommings.campaigner.locations.dtos.TerrainDTO;
import com.mcommings.campaigner.locations.services.interfaces.ITerrain;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.mcommings.campaigner.enums.ErrorMessage.ID_NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/terrains")
public class TerrainController {

    private final ITerrain terrainService;

    @GetMapping
    public List<TerrainDTO> getTerrains() {
        return terrainService.getTerrains();
    }

    @GetMapping(path = "/{terrainId}")
    public TerrainDTO getTerrain(@PathVariable("terrainId") int terrainId) {
        return terrainService.getTerrain(terrainId).orElseThrow(() -> new IllegalArgumentException(ID_NOT_FOUND.message));
    }

    @PostMapping
    public void saveTerrain(@Valid @RequestBody TerrainDTO terrain) {
        terrainService.saveTerrain(terrain);
    }

    @DeleteMapping(path = "{terrainId}")
    public void deleteTerrain(@PathVariable("terrainId") int terrainId) {
        terrainService.deleteTerrain(terrainId);
    }

    @PutMapping(path = "{terrainId}")
    public void updateTerrain(@PathVariable("terrainId") int terrainId, @RequestBody TerrainDTO terrain) {
        terrainService.updateTerrain(terrainId, terrain);
    }
}
