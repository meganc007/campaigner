package com.mcommings.campaigner.locations.sublocations;

import com.mcommings.campaigner.models.Government;
import lombok.Data;

@Data
public class City {
    private long id;
    private String name;
    private String type;
    private Government government;
    private String defense;

    public City(long id, String name, String type, Government government, String defense) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.government = government;
        this.defense = defense;
    }
}
