package com.mcommings.campaigner.controllers.locations;

import com.mcommings.campaigner.models.locations.Region;
import com.mcommings.campaigner.services.locations.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/locations/regions")
public class RegionController {

    private final RegionService regionService;

    @Autowired
    public RegionController(RegionService regionService) {
        this.regionService = regionService;
    }

    @GetMapping
    public List<Region> getRegions() {
        return regionService.getRegions();
    }

    @PostMapping
    public void saveRegion(@RequestBody Region region) {
        regionService.saveRegion(region);
    }

    @DeleteMapping(path = "{regionId}")
    public void deleteRegion(@PathVariable("regionId") int regionId) {
        regionService.deleteRegion(regionId);
    }

    @PutMapping(path = "{regionId}")
    public void updateRegion(@PathVariable("regionId") int regionId, @RequestBody Region region) {
        regionService.updateRegion(regionId, region);
    }
}
