package com.mcommings.campaigner.models.locations;

import com.mcommings.campaigner.models.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "landmarks")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@ToString(callSuper = true)
public class Landmark extends BaseEntity {

    //TODO: add fk_region

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "fk_region")
    private Integer fk_region;

    public Landmark() {
        super();
    }

    public Landmark(int id, String name, String description) {
        this.id = id;
        super.setName(name);
        super.setDescription(description);
    }

    public Landmark(int id, String name, String description, Integer fk_region) {
        this.id = id;
        super.setName(name);
        super.setDescription(description);
        this.fk_region = fk_region;
    }
}
