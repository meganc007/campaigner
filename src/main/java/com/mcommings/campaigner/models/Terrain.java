package com.mcommings.campaigner.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "terrains")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@ToString(callSuper = true)
public class Terrain extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    public Terrain() {
        super();
    }

    public Terrain(int id, String name, String description) {
        this.id = id;
        this.setName(name);
        this.setDescription(description);
    }
}
