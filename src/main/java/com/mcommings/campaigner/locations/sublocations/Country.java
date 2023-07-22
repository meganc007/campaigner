package com.mcommings.campaigner.locations.sublocations;

import com.mcommings.campaigner.governments.Government;
import lombok.Data;

import java.util.List;

@Data
public class Country {
    private long id;
    private String name;
    private String description;
    private Continent continent;
    private Government government;

    public Country(long id, String name, Government government, String description) {
        this.id = id;
        this.name = name;
        this.government = government;
        this.description = description;
    }
}
