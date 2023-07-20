package com.mcommings.campaigner.locations.sublocations;

import com.mcommings.campaigner.governments.Government;
import lombok.Data;

import java.util.List;

@Data
public class Country {
    private long id;
    private String name;
    private Government government;
    private String description;
    private String region;
    private String climate;
    private List<City> cities;

    public Country(long id, String name, Government government, String description, String region, String climate, List<City> cities) {
        this.id = id;
        this.name = name;
        this.government = government;
        this.description = description;
        this.region = region;
        this.climate = climate;
        this.cities = cities;
    }
}
