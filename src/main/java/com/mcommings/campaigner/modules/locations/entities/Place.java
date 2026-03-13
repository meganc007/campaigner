package com.mcommings.campaigner.modules.locations.entities;

import com.mcommings.campaigner.modules.common.entities.Campaign;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@Table(name = "places",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"name", "fk_campaign_uuid"}
                )
        })
@NoArgsConstructor
@AllArgsConstructor
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_campaign_uuid", nullable = false)
    private Campaign campaign;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_place_type", nullable = false)
    private PlaceType placeType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_terrain", nullable = false)
    private Terrain terrain;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_country", nullable = false)
    private Country country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_city")
    private City city;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_region", nullable = false)
    private Region region;

}
