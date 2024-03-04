package com.mcommings.campaigner.controllers.locations;

import com.mcommings.campaigner.models.locations.Landmark;
import com.mcommings.campaigner.services.locations.LandmarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/locations/landmarks")
public class LandmarkController {

    private final LandmarkService landmarkService;

    @Autowired
    public LandmarkController(LandmarkService landmarkService) {
        this.landmarkService = landmarkService;
    }

    @GetMapping
    public List<Landmark> getLandmarks() {
        return landmarkService.getLandmarks();
    }

    @GetMapping(path = "/{landmarkId}")
    public Landmark getLandmark(@PathVariable("landmarkId") int landmarkId) {
        return landmarkService.getLandmark(landmarkId);
    }

    @GetMapping(path = "/campaign/{uuid}")
    public List<Landmark> getLandmarksByCampaignUUID(@PathVariable("uuid") UUID uuid) {
        return landmarkService.getLandmarksByCampaignUUID(uuid);
    }

    @GetMapping(path = "/region/{regionId}")
    public List<Landmark> getLandmarksByRegionId(@PathVariable("regionId") int regionId) {
        return landmarkService.getLandmarksByRegionId(regionId);
    }

    @PostMapping
    public void saveLandmark(@RequestBody Landmark landmark) {
        landmarkService.saveLandmark(landmark);
    }

    @DeleteMapping(path = "{landmarkId}")
    public void deleteLandmark(@PathVariable("landmarkId") int landmarkId) {
        landmarkService.deleteLandmark(landmarkId);
    }

    @PutMapping(path = "{landmarkId}")
    public void updateLandmark(@PathVariable("landmarkId") int landmarkId, @RequestBody Landmark landmark) {
        landmarkService.updateLandmark(landmarkId, landmark);
    }
}
