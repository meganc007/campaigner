package com.mcommings.campaigner.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "races")
public class Race {

    @Id
    @SequenceGenerator(name = "races_id_seq", sequenceName = "races_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "races_id_seq")
    private int id;
    private String name;
    private String description;
    private boolean is_exotic;

    public Race() {}

    public Race(int id, String name, String description, boolean is_exotic) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.is_exotic = is_exotic;
    }
}
