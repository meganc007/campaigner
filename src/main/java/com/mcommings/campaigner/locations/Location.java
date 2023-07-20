package com.mcommings.campaigner.locations;

import com.mcommings.campaigner.locations.sublocations.Continent;
import lombok.Data;

import java.util.List;

@Data
public class Location {
    private long id;
    private List<Continent> continents;

    public Location(long id, List<Continent> continents) {
        this.id = id;
        this.continents = continents;
    }
}
