package com.mcommings.campaigner.locations.controllers;

import com.mcommings.campaigner.locations.dtos.LandmarkDTO;
import com.mcommings.campaigner.locations.services.interfaces.ILandmark;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.mcommings.campaigner.enums.ErrorMessage.ID_NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/landmarks")
public class LandmarkController {

    private final ILandmark landmarkService;

    @GetMapping
    public List<LandmarkDTO> getLandmarks() {
        return landmarkService.getLandmarks();
    }

    @GetMapping(path = "/{landmarkId}")
    public LandmarkDTO getLandmark(@PathVariable("landmarkId") int landmarkId) {
        return landmarkService.getLandmark(landmarkId).orElseThrow(() -> new IllegalArgumentException(ID_NOT_FOUND.message));
    }

    @GetMapping(path = "/campaign/{uuid}")
    public List<LandmarkDTO> getLandmarksByCampaignUUID(@PathVariable("uuid") UUID uuid) {
        return landmarkService.getLandmarksByCampaignUUID(uuid);
    }

    @GetMapping(path = "/region/{regionId}")
    public List<LandmarkDTO> getLandmarksByRegionId(@PathVariable("regionId") int regionId) {
        return landmarkService.getLandmarksByRegionId(regionId);
    }

    @PostMapping
    public void saveLandmark(@Valid @RequestBody LandmarkDTO landmark) {
        landmarkService.saveLandmark(landmark);
    }

    @DeleteMapping(path = "{landmarkId}")
    public void deleteLandmark(@PathVariable("landmarkId") int landmarkId) {
        landmarkService.deleteLandmark(landmarkId);
    }

    @PutMapping(path = "{landmarkId}")
    public void updateLandmark(@PathVariable("landmarkId") int landmarkId, @RequestBody LandmarkDTO landmark) {
        landmarkService.updateLandmark(landmarkId, landmark);
    }
}
