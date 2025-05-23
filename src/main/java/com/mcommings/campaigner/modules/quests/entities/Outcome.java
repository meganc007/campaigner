package com.mcommings.campaigner.modules.quests.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@Table(name = "outcomes")
@NoArgsConstructor
@AllArgsConstructor
public class Outcome {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String description;
    private String notes;
    @Column(name = "fk_campaign_uuid")
    private UUID fk_campaign_uuid;
}
