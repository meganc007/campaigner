package com.mcommings.campaigner.modules.overview.controllers;

import com.mcommings.campaigner.modules.overview.dtos.LocationOverviewDTO;
import com.mcommings.campaigner.modules.overview.services.interfaces.ILocationOverview;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/locations")
public class LocationOverviewController {

    private final ILocationOverview locationService;

    @GetMapping(path = "/{uuid}")
    public LocationOverviewDTO getLocationOverview(@PathVariable("uuid") UUID uuid) {
        return locationService.getLocationOverview(uuid);
    }
}
