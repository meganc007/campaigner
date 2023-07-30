package com.mcommings.campaigner.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "governments")
public class Government {
    @Id
    @SequenceGenerator(name = "governments_id_seq", sequenceName = "governments_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "governments_id_seq")
    private int id;
    private String name;
    private String description;

    public Government () {}

    public Government(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

}
