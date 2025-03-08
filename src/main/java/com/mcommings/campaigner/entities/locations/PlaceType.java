package com.mcommings.campaigner.entities.locations;

import com.mcommings.campaigner.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
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
