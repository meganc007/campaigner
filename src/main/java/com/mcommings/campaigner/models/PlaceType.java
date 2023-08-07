package com.mcommings.campaigner.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "place_types")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@ToString(callSuper = true)
public class PlaceType extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public PlaceType() {
        super();
    }

    public PlaceType(int id, String name, String description) {
        this.id = id;
        this.setName(name);
        this.setDescription(description);
    }
}
