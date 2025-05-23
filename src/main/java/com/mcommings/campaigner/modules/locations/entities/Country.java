package com.mcommings.campaigner.modules.locations.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@Table(name = "countries")
@NoArgsConstructor
@AllArgsConstructor
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false, unique = true)
    private String name;
    private String description;
    @Column(nullable = false)
    private UUID fk_campaign_uuid;
    private Integer fk_continent;
    private Integer fk_government;
}
