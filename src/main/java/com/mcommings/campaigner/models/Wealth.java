package com.mcommings.campaigner.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "wealth")
public class Wealth {

    @Id
    @SequenceGenerator(name = "wealth_id_seq", sequenceName = "wealth_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "wealth_id_seq")
    private int id;
    private String name;

    public Wealth() {}

    public Wealth(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
