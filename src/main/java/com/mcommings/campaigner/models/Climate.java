package com.mcommings.campaigner.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "climates")
public class Climate {

    @Id
    @SequenceGenerator(name = "climates_id_seq", sequenceName = "climates_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "climates_id_seq")
    private int id;
    private String name;
    private String description;

    public Climate () {}

    public Climate(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
