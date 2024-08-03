package com.mcommings.campaigner.models.locations;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class Location {
    private UUID campaignUuid;
    private List<Continent> continents;
    private List<Country> countries;
    private List<City> cities;
    private List<Place> places;
    private List<Landmark> landmarks;

    public Location() {
    }

    public Location(UUID campaignUuid) {
        this.campaignUuid = campaignUuid;
    }
}
