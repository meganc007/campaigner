package com.mcommings.campaigner.models.locations;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mcommings.campaigner.models.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "terrains")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@ToString(callSuper = true)
public class Terrain extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JsonManagedReference
    @OneToMany(mappedBy = "terrain")
    private Set<Place> places = new HashSet<>();

    public Terrain() {
        super();
    }

    public Terrain(int id, String name, String description) {
        this.id = id;
        this.setName(name);
        this.setDescription(description);
    }
}
