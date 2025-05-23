package com.mcommings.campaigner.modules.quests.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@Table(name = "rewards")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@NoArgsConstructor
@AllArgsConstructor
public class Reward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String description;
    private String notes;
    @Column(name = "fk_campaign_uuid")
    private UUID fk_campaign_uuid;

    private int gold_value;
    private int silver_value;
    private int copper_value;
    private Integer fk_item;
    private Integer fk_weapon;

}
