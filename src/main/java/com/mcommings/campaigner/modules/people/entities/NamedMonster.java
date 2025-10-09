package com.mcommings.campaigner.modules.people.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@Table(name = "named_monsters")
@NoArgsConstructor
@AllArgsConstructor
public class NamedMonster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String title;
    private Integer fk_wealth;
    private Integer fk_ability_score;
    private Integer fk_generic_monster;
    @Column(name = "isenemy", nullable = false)
    private Boolean isEnemy;
    private String personality;
    private String description;
    private String notes;
    @Column(nullable = false)
    private UUID fk_campaign_uuid;
}
