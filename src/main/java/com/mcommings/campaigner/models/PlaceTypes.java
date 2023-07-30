package com.mcommings.campaigner.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "place_types")
public class PlaceTypes {

    @Id
    @SequenceGenerator(name = "place_types_id_seq", sequenceName = "place_types_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "place_types_id_seq")
    private int id;
    private String name;
    private String description;

    public PlaceTypes() {}

    public PlaceTypes(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
