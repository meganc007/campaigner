package com.mcommings.campaigner.controllers.locations;

import com.mcommings.campaigner.models.locations.Terrain;
import com.mcommings.campaigner.services.locations.TerrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/locations/terrains")
public class TerrainController {

    private final TerrainService terrainService;

    @Autowired
    public TerrainController(TerrainService terrainService) {this.terrainService = terrainService;}

    @GetMapping
    public List<Terrain> getTerrains() {
        return terrainService.getTerrains();
    };

    @PostMapping
    public void saveTerrain(@RequestBody Terrain terrain) {
        terrainService.saveTerrain(terrain);
    }

    @DeleteMapping(path = "{terrainId}")
    public void deleteTerrain(@PathVariable("terrainId") int terrainId) {
        terrainService.deleteTerrain(terrainId);
    }

    @PutMapping(path = "{terrainId}")
    public void updateTerrain(@PathVariable("terrainId") int terrainId, @RequestBody Terrain terrain) {
        terrainService.updateTerrain(terrainId, terrain);
    }
}
