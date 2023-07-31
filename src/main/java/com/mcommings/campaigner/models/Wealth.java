package com.mcommings.campaigner.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "wealth")
public class Wealth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    public Wealth() {}

    public Wealth(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
