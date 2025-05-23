package com.mcommings.campaigner.modules.items.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@Table(name = "weapons")
@NoArgsConstructor
@AllArgsConstructor
public class Weapon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false, unique = true)
    private String name;
    private String description;
    @Column(nullable = false)
    private UUID fk_campaign_uuid;
    private String rarity;
    private int gold_value;
    private int silver_value;
    private int copper_value;
    private float weight;
    private Integer fk_weapon_type;
    private Integer fk_damage_type;
    private Integer fk_dice_type;
    private int number_of_dice;
    private int damage_modifier;
    private Boolean isMagical;
    private Boolean isCursed;
    private String notes;
}
