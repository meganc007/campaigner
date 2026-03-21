package com.mcommings.campaigner.modules.locations.controllers;

import com.mcommings.campaigner.modules.locations.dtos.landmarks.CreateLandmarkDTO;
import com.mcommings.campaigner.modules.locations.dtos.landmarks.UpdateLandmarkDTO;
import com.mcommings.campaigner.modules.locations.dtos.landmarks.ViewLandmarkDTO;
import com.mcommings.campaigner.modules.locations.services.LandmarkService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/landmarks")
public class LandmarkController {

    private final LandmarkService landmarkService;

    @GetMapping
    public List<ViewLandmarkDTO> getLandmarks() {

        return landmarkService.getAll();
    }

    @GetMapping(path = "/{landmarkId}")
    public ViewLandmarkDTO getLandmark(@PathVariable int landmarkId) {
        return landmarkService.getById(landmarkId);
    }

    @GetMapping(path = "/campaign/{uuid}")
    public List<ViewLandmarkDTO> getLandmarksByCampaignUUID(@PathVariable UUID uuid) {
        return landmarkService.getLandmarksByCampaignUUID(uuid);
    }

    @GetMapping(path = "/region/{regionId}")
    public List<ViewLandmarkDTO> getLandmarksByRegionId(@PathVariable int regionId) {
        return landmarkService.getLandmarksByRegionId(regionId);
    }

    @PostMapping
    public ViewLandmarkDTO createLandmark(@Valid @RequestBody CreateLandmarkDTO landmark) {

        return landmarkService.create(landmark);
    }

    @PutMapping
    public ViewLandmarkDTO updateLandmark(@Valid @RequestBody UpdateLandmarkDTO landmark) {
        return landmarkService.update(landmark);
    }

    @DeleteMapping(path = "/{landmarkId}")
    public void deleteLandmark(@PathVariable int landmarkId) {
        landmarkService.delete(landmarkId);
    }


}
