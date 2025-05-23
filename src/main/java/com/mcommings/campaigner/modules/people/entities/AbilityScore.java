package com.mcommings.campaigner.modules.people.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@Table(name = "ability_scores")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@NoArgsConstructor
@AllArgsConstructor
public class AbilityScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private UUID fk_campaign_uuid;
    @Column(nullable = false)
    private int strength;
    @Column(nullable = false)
    private int dexterity;
    @Column(nullable = false)
    private int constitution;
    @Column(nullable = false)
    private int intelligence;
    @Column(nullable = false)
    private int wisdom;
    @Column(nullable = false)
    private int charisma;
}
