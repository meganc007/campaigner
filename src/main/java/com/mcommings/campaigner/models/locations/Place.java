package com.mcommings.campaigner.models.locations;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mcommings.campaigner.models.BaseEntity;
import com.mcommings.campaigner.models.Campaign;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "places")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@ToString(callSuper = true)
public class Place extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "fk_campaign_uuid")
    private UUID fk_campaign_uuid;

    @Column(name = "fk_place_type")
    private Integer fk_place_type;

    @Column(name = "fk_terrain")
    private Integer fk_terrain;

    @Column(name = "fk_country")
    private Integer fk_country;

    @Column(name = "fk_city")
    private Integer fk_city;

    @Column(name = "fk_region")
    private Integer fk_region;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "fk_campaign_uuid", referencedColumnName = "campaign_uuid", updatable = false, insertable = false)
    private Campaign campaign;

    @ManyToOne
    @JoinColumn(name = "fk_place_type", referencedColumnName = "id", updatable = false, insertable = false)
    private PlaceType placeType;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "fk_terrain", referencedColumnName = "id", updatable = false, insertable = false)
    private Terrain terrain;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "fk_country", referencedColumnName = "id", updatable = false, insertable = false)
    private Country country;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "fk_city", referencedColumnName = "id", updatable = false, insertable = false)
    private City city;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "fk_region", referencedColumnName = "id", updatable = false, insertable = false)
    private Region region;

    public Place() {
        super();
    }

    public Place(int id, String name, String description, UUID fk_campaign_uuid) {
        this.id = id;
        this.setName(name);
        this.setDescription(description);
        this.fk_campaign_uuid = fk_campaign_uuid;
    }

    public Place(int id, String name, String description, UUID fk_campaign_uuid,
                 Integer fk_place_type, Integer fk_terrain, Integer fk_country,
                 Integer fk_city, Integer fk_region) {
        this.id = id;
        this.setName(name);
        this.setDescription(description);
        this.fk_campaign_uuid = fk_campaign_uuid;
        this.fk_place_type = fk_place_type;
        this.fk_terrain = fk_terrain;
        this.fk_country = fk_country;
        this.fk_city = fk_city;
        this.fk_region = fk_region;
    }

}
