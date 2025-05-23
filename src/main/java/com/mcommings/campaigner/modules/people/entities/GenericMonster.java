package com.mcommings.campaigner.modules.people.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@Table(name = "generic_monsters")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@NoArgsConstructor
@AllArgsConstructor
public class GenericMonster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false, unique = true)
    private String name;
    private String description;
    private Integer fk_ability_score;
    private String traits;
    private String notes;
    @Column(nullable = false)
    private UUID fk_campaign_uuid;
}
