package com.mcommings.campaigner.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "places")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@ToString(callSuper = true)
public class Place extends BaseEntity {

    //TODO: add fk_region

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "fk_place_type")
    private Integer fk_place_type;

    @Column(name = "fk_terrain")
    private Integer fk_terrain;

    @Column(name = "fk_country")
    private Integer fk_country;

    @Column(name = "fk_city")
    private Integer fk_city;

    @ManyToOne
    @JoinColumn(name = "fk_place_type", referencedColumnName = "id", updatable = false, insertable = false)
    private PlaceType placeType;

    @ManyToOne
    @JoinColumn(name = "fk_terrain", referencedColumnName = "id", updatable = false, insertable = false)
    private Terrain terrain;

    @ManyToOne
    @JoinColumn(name = "fk_country", referencedColumnName = "id", updatable = false, insertable = false)
    private Country country;

    @ManyToOne
    @JoinColumn(name = "fk_city", referencedColumnName = "id", updatable = false, insertable = false)
    private City city;

    public Place() {
        super();
    }

    public Place(int id, String name, String description) {
        this.id = id;
        this.setName(name);
        this.setDescription(description);
    }

    public Place(int id, String name, String description,
                 Integer fk_place_type, Integer fk_terrain,
                 Integer fk_country, Integer fk_city) {
        this.id = id;
        this.setName(name);
        this.setDescription(description);
        this.fk_place_type = fk_place_type;
        this.fk_terrain = fk_terrain;
        this.fk_country = fk_country;
        this.fk_city = fk_city;
    }

}
