package com.mcommings.campaigner.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "terrains")
public class Terrain {

    @Id
    @SequenceGenerator(name = "terrains_id_seq", sequenceName = "terrains_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "terrains_id_seq")
    private int id;
    private String name;
    private String description;

    public Terrain() {}

    public Terrain(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
