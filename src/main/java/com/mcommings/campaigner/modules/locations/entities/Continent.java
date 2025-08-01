package com.mcommings.campaigner.modules.locations.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@Table(name = "continents")
@NoArgsConstructor
@AllArgsConstructor
public class Continent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String name;
    private String description;
    @Column(nullable = false)
    private UUID fk_campaign_uuid;

}
