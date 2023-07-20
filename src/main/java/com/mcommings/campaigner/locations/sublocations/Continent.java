package com.mcommings.campaigner.locations.sublocations;

import lombok.Data;

import java.util.List;

@Data
public class Continent {
    private long id;
    private String name;
    private List<Country> countries;

    public Continent(long id, String name, List<Country> countries) {
        this.id = id;
        this.name = name;
        this.countries = countries;
    }
}
