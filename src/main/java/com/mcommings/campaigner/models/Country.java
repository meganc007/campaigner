package com.mcommings.campaigner.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "countries")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@ToString(callSuper = true)
public class Country extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "fk_continent")
    private Integer continentId;

    @Column(name = "fk_government")
    private Integer governmentId;

    public Country() {
        super();
    }

    public Country(int id, String name, String description) {
        this.id = id;
        this.setName(name);
        this.setDescription(description);
    }
    public Country(int id, String name, String description, int continentId, int governmentId) {
        this.id = id;
        this.setName(name);
        this.setDescription(description);
        this.continentId = continentId;
        this.governmentId = governmentId;
    }
}
