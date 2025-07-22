package com.mcommings.campaigner.modules.people.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@Table(name = "people")
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private int age;
    private String title;
    private Integer fk_race;
    private Integer fk_wealth;
    private Integer fk_ability_score;

    @Column(name = "isnpc", nullable = false)
    private Boolean isNPC;

    @Column(name = "isenemy", nullable = false)
    private Boolean isEnemy;

    private String personality;
    private String description;
    private String notes;

    @Column(name = "fk_campaign_uuid", nullable = false)
    private UUID fk_campaign_uuid;
}
