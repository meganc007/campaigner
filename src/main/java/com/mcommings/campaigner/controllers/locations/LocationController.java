package com.mcommings.campaigner.controllers.locations;

import com.mcommings.campaigner.models.locations.Location;
import com.mcommings.campaigner.services.locations.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(path = "api/locations")
public class LocationController {

    private final LocationService locationService;

    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("/{uuid}")
    public Location getLocation(@PathVariable("uuid") UUID uuid) {
        return locationService.getLocation(uuid);
    }
}
