package com.mcommings.campaigner.controllers;

import com.mcommings.campaigner.models.Terrain;
import com.mcommings.campaigner.services.TerrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
